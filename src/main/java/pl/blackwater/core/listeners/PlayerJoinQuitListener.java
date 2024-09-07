package pl.blackwater.core.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Combat;
import pl.blackwater.core.data.RankSet;
import pl.blackwater.core.data.User;
import pl.blackwater.core.halloween.HalloweenManager;
import pl.blackwater.core.halloween.HalloweenPumpkin;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.interfaces.Teleportable;
import pl.blackwater.core.managers.*;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.core.settings.TabListConfig;
import pl.blackwater.core.tablist.AbstractTablist;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwater.core.utils.PlayerInventoryUtil;
import pl.blackwater.enchantgui.Main;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.ranking.RankingManager;
import pl.blackwater.guilds.scoreboard.ScoreBoardNameTag;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.IncognitoUserUtil;
import pl.blackwaterapi.utils.Util;
import pl.blazingpack.waypoints.WaypointBuilder;
import pl.blazingpack.waypoints.WaypointManager;
import pl.justsectors.nbt.NBTManager;
import pl.justsectors.packets.impl.user.JoinSectorPacket;
import pl.justsectors.packets.impl.user.UserRegisterPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerJoinQuitListener implements Listener,Colors{


	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		TeleportManager.registerJoinDelay(p.getUniqueId());
		final Sector sector = SectorManager.getCurrentSector().get();
		sector.getOnlinePlayers().add(p.getName());
		RedisClient.sendSectorsPacket(new JoinSectorPacket(p.getName(), sector.getSectorName()));
		IncognitoUserUtil.join(e);
		final Combat combat = CombatManager.getCombat(p);
		if(combat == null)
			CombatManager.CreateCombat(p);
		User u = UserManager.getUser(p);
		if(u != null) {
			if(u.getNbtString() == null)
			{
				final NBTTagCompound nbtTagCompound = NBTManager.createNBTBasedOnUser(u);
				if(nbtTagCompound == null)
				{
					p.kickPlayer("Blad podczas zapisu Twojego ekwipunku!");
					return;
				}
				p.kickPlayer("Blad podczas ladowania Twojego profilu! Prosimy relognac!");
				return;
			}
			final NBTTagCompound nbtTagCompound = NBTManager.asNBT(u.getNbtString());
			if(nbtTagCompound == null)
			{
				p.kickPlayer("Blad podczas ladowania Twojego profilu!");
				return;
			}
			NBTManager.applyToPlayer(u, nbtTagCompound);
			final Location last = u.getLastLocation();
			p.teleport(last);
			final Teleportable teleportable = TeleportManager.getRequest(u.getUuid());
			if (teleportable != null) {
				p.teleport(teleportable.getLocation());
				p.sendMessage(Util.fixColor("&8>> &7Teleportacja przebiegla pomyslnie!"));
				TeleportManager.deleteRequestIfExists(u.getUuid());
			}
		}

		if(u == null) {
			PlayerInventoryUtil.DefaultPlayerInventory(p);
			if(CoreConfig.TELEPORTMANAGER_RANDOMSPAWNONJOIN) {
				p.teleport(LocationUtil.getRandSpawnLocation(CoreConfig.getSpawnLocations()));
			}
			if(CoreConfig.TELEPORTMANAGER_RANDOMWARPONJOIN){
				WarpManager.getRandomWarpFastTP(p);
			}
			if(!CoreConfig.TELEPORTMANAGER_RANDOMWARPONJOIN && !CoreConfig.TELEPORTMANAGER_RANDOMSPAWNONJOIN){
				p.teleport(p.getWorld().getSpawnLocation());
			}
			u = UserManager.createUser(p);
			final NBTTagCompound nbtTagCompound = NBTManager.createNBTBasedOnUser(u);
			if(nbtTagCompound == null) {
				p.kickPlayer("&cBlad podczas ladowania Twojego profilu... relognij!");
				return;
			}
			u.setNbtString(nbtTagCompound.toString());
			RedisClient.sendSectorsPacket(new UserRegisterPacket(u.getLastName(), u.getUuid(), u.getFirstIP(), u.getGameMode(), u.isFly(), u.getLastLocation(), u.getHomeLocation()));
			RankingManager.addRanking(u);
		}
		AbstractTablist abstractTablist = AbstractTablist.createTablist(TabListConfig.playerList,TabListConfig.TABLIST_HEADER,TabListConfig.TABLIST_FOOTER,9999,p);
		abstractTablist.send();

		UserManager.joinToGame(p);
		ChatControlUserManager.joinToGame(u);
		Core.getChatManager().clearPlayerChat(p, 10);
		for(String s : MessageConfig.MESSAGE_JOIN)
			Util.sendMsg(p, Util.fixColor(Util.replaceString(s.replace("{PLAYER}", p.getDisplayName())
	    			  .replace("{REGISTERPLAYERS}", (UserManager.getUsers().size() > CoreConfig.getFakePlayersOnline() ? String.valueOf(UserManager.getUsers().size()) : String.valueOf(CoreConfig.getFakePlayersOnline())))
	    			  .replace("{ONLINE}", String.valueOf(Bukkit.getOnlinePlayers()))
	    			  .replace("{FAKEONLINE}", String.valueOf(CoreConfig.getFakePlayersOnline()))
	    			  .replace("{SLOTS}", String.valueOf(CoreConfig.SLOTMANAGER_SLOTS)))));
		final RankSet rankSet = RankSetManager.getSetRank(u);
		if(rankSet != null) {
			if(rankSet.getExpireTime() != 0L && rankSet.getExpireTime() <= System.currentTimeMillis()){
				ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Twoja ranga " + ImportantColor + rankSet.getRank() + MainColor + " wlasnie " + ImportantColor + " wygasla")));
				RankSetManager.removeRank(u, rankSet);
			}else {
				ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Posiadasz range " + ImportantColor + rankSet.getRank() + MainColor + " " + (rankSet.getExpireTime() == 0L ? "na " + ImportantColor + "zawsze" : "do " + ImportantColor + Util.getDate(rankSet.getExpireTime())))), 20 * 5);
			}
		}
		ScoreBoardNameTag.initPlayer(p);
		ScoreBoardNameTag.updateBoard(p);
		EasyScoreboardManager.createScoreBoard(p);
		WaypointManager.deleteAllWaypoints(p);
		for (HalloweenPumpkin activePumpkin : HalloweenManager.getActivePumpkins()) {
			WaypointBuilder.builder().withAction((byte) 0)
					.withBlockPosition(System.currentTimeMillis())
					.withRgb(16711680)
					.withLocation(activePumpkin.getLocation().clone().add(0,1,0))
					.withTime(TimeUnit.DAYS.toMillis(1))
					.withWaypointID(activePumpkin.getPumpkinID())
					.withWaypointName(activePumpkin.getPumpkinName())
					.withType(0)
					.withImage_hash("80b80033421018fa314ef50c176331b71b3655ef")
					.withImage_size(9120).build().sendPlayer(p);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinLater(PlayerJoinEvent e) {
		e.setJoinMessage(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickLater(PlayerKickEvent e)
	{
		e.setLeaveMessage(null);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerKick(PlayerKickEvent e)
	{
		final Player p = e.getPlayer();
		ScoreBoardNameTag.removeBoard(p);
		quitGame(p);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		ScoreBoardNameTag.removeBoard(p);
		SectorManager.getCurrentSector().get().getOnlinePlayers().remove(p.getName());
		quitGame(p);
		UserManager.leaveFromGame(p, true);
		AbstractTablist.removeTablist(p);
		EasyScoreboardManager.removeScoreBoard(p);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuitLater(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}
	
	private static void quitGame(Player p) {
		Combat combat = CombatManager.getCombat(p);
		if(combat == null)
			return;
		if(!combat.hasFight())
			return;
		p.setHealth(0.0D);
		p.getInventory().clear();
		Guild g = GuildManager.getGuild(p);
		Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + BOLD + "ANTYLOGOUT" + SpecialSigns + " * " + MainColor + "Gracz " + (g == null ? "" : SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "]") +  ImportantColor + p.getName() + MainColor + " wylogowal sie podczas " + ImportantColor + "walki")));
		User u = UserManager.getUser(p);
		u.addLogoutViaPacket(1);
	}
	
}
