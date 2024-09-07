package pl.blackwaterapi.utils;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.blackwaterapi.objects.IncognitoUser;

import java.util.UUID;

public class NameUtil {
	
	private static NameUtil instance;
	
	public NameUtil() {
		instance = this;
	}
	
	public static NameUtil getInstance() {
		if(instance == null) new NameUtil();
		return instance;
	}
	
	public void updateCommand(Player player, Boolean status) {
		IncognitoUser IncognitoUser = IncognitoUserUtil.get(player);
		Packet[] packets = constructPackets(player, IncognitoUser.getChangeNick(), status);
		for(Player target : Bukkit.getOnlinePlayers()) {
			if(target != player && !target.hasPermission("nanoincognito.bypass")) send(target, packets);
		}
		
	}
	
	public void updateJoin(Player player) {
		IncognitoUser IncognitoUser = IncognitoUserUtil.get(player);
		if(IncognitoUser.getTextures() == null) IncognitoUser.setTextures(getPropertySkin(player));
		if(player.hasPermission("nanoincognito.bypass")) return;
		for(Player target : Bukkit.getOnlinePlayers()) {
			IncognitoUser t = IncognitoUserUtil.get(target);
			if(!t.getChangeNick().equalsIgnoreCase(t.getName())) send(player, constructPackets(target, t.getChangeNick(), false));
		}
	}
	
	private void send(Player target, Packet[] packets) {
		PlayerConnection pc = ((CraftPlayer) target).getHandle().playerConnection;
		for(Packet p : packets) pc.sendPacket(p);
	}
	
	private Property getPropertySkin(Player player) {
		GameProfile gp =  ((CraftPlayer) player).getHandle().getProfile();
		if(gp.getProperties().containsKey("textures")) return gp.getProperties().get("textures").iterator().next();
		return null;
	}
	
	public void restart(Player player) {
		IncognitoUser IncognitoUser = IncognitoUserUtil.get(player);
		Packet[] packets = constructPackets(player, IncognitoUser.getName(), true);
		for(Player target : Bukkit.getOnlinePlayers()) {
			if(target != player && !target.hasPermission("nanoincognito.bypass")) send(target, packets);
		}
	}
	
	private Packet[] constructPackets(Player player, String nick, Boolean status) {
		EntityPlayer ep = ((CraftPlayer) player).getHandle();
		EntityPlayer s = constructWithName(ep, nick, status);
		Packet[] packets = new Packet[4];
		packets[0] = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ep);
		packets[1] = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, s);
		packets[2] = new PacketPlayOutEntityDestroy(ep.getId());
		packets[3] = new PacketPlayOutNamedEntitySpawn(ep);
		return packets;
	}
	
	private GameProfile constructWithName(UUID uuid, String nick, Boolean status) {
		GameProfile gp = new GameProfile(uuid, nick);
		if(status ) {
			IncognitoUser IncognitoUser = IncognitoUserUtil.getByNick(nick);
			if(IncognitoUser.getTextures() != null) {
				gp.getProperties().put("textures", IncognitoUser.getTextures());
			}
			return gp;
		}
		if(gp.getProperties().containsKey("textures")) gp.getProperties().asMap().remove("textures");
		return gp;
	}
	
	private EntityPlayer constructWithName(EntityPlayer ep, String nick, Boolean status) {
		GameProfile gp = constructWithName(ep.getUniqueID(), nick, status);
		EntityPlayer s = new EntityPlayer(MinecraftServer.getServer(), ep.getWorld().getWorld().getHandle(), gp, new PlayerInteractManager(ep.getWorld().getWorld().getHandle()));
		s.locX = ep.locX;
		s.locY = ep.locY;
		s.locZ = ep.locZ;
		s.yaw = ep.yaw;
		s.pitch = ep.pitch;
		return s;
	}

}
