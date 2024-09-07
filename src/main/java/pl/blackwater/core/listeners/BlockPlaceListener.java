package pl.blackwater.core.listeners;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.utils.TimeUtil;
import pl.blackwaterapi.utils.Util;

public class BlockPlaceListener implements Listener,Colors{
	private final static List<Material> blockMaterials = Arrays.asList(Material.TRAPPED_CHEST,Material.STORAGE_MINECART,Material.HOPPER,Material.HOPPER_MINECART,Material.RAILS,Material.ACTIVATOR_RAIL,Material.DETECTOR_RAIL,Material.POWERED_RAIL,Material.BREWING_STAND);
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onArenaBlockPlace(BlockPlaceEvent e) {
		final Player player = e.getPlayer();
		if(e.getItemInHand().getType().equals(Material.BARRIER)){
			if(e.getBlock().getLocation().add(0.0D,1.0D,0.0D).getBlock().getType().equals(Material.ENDER_STONE)){
				e.setCancelled(true);
				return;
			}
			Location location = e.getBlockPlaced().getLocation();
			for(int i = 0; i < 250;i++){
				if(location.getWorld().getBlockAt(location.add(0, 1, 0)).getType() != Material.BEDROCK){
					location.getBlock().setType(Material.BARRIER);
				}
			}
			Location location2 = e.getBlockPlaced().getLocation();
			for(int i = 0; i < 100;i++){
				if(location2.getWorld().getBlockAt(location2.subtract(0, 1, 0)).getType() == Material.AIR){
					location2.getBlock().setType(Material.BARRIER);
				}
			}
		}
		if(!player.hasPermission("core.admin")) {
			final LocalPlayer lplayer = Objects.requireNonNull(Core.getWorldGuard()).wrapPlayer(player);
			final ApplicableRegionSet applicableRegionSet = Core.getWorldGuard().getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
			if (!applicableRegionSet.canBuild(lplayer)) {
				player.setGameMode(GameMode.ADVENTURE);
				Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz budowac na tym terenie!");
				Bukkit.getScheduler().runTaskLater(Core.getPlugin(), () -> player.setGameMode(GameMode.SURVIVAL), TimeUtil.SECOND.getTick(15));
			}
		}
		if(e.isCancelled())
			return;
		final Player p = e.getPlayer();
		if(p.hasPermission("core.admin"))
			return;
		final Material type = e.getBlock().getType();
		if(blockMaterials.contains(type)) {
			e.setCancelled(true);
			Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Ten blok zostal zablokowany na serwerze!");
		}
		if(e.getBlock().getType().equals(Material.CHEST)) {
			final User u = UserManager.getUser(p);
			final int canplaced = (u.getBoostChest() == 0 ? 4 : (u.getBoostChest() == 1 ? 6 : (u.getBoostChest() == 2 ? 10 : (u.getBoostChest() == 3 ? 16 : (u.getBoostChest() == 4 ? 20 : (u.getBoostChest() == 5 ? 26 : 36))))));
			if (u.getPlaceChest() < canplaced) {
				u.addPlaceChestViaPacket(1);
				Util.sendMsg(p, Util.replaceString(SpecialSigns + ">> " + MainColor + "Polozyles skrzynke, mozesz postawic jeszcze " + ImportantColor + (canplaced - u.getPlaceChest()) + MainColor + " skrzynek"));
			} else {
				Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Limit postawionych skrzynek wyczerpal sie " + SpecialSigns + "(" + WarningColor + u.getPlaceChest() + "/" + canplaced + SpecialSigns + ")" + WarningColor_2 + ", aby go powiekszyc kup boost pod /sklep");
				e.setCancelled(true);
			}
		}
	}
	
}
