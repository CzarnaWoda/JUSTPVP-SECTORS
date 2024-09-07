package pl.blackwater.storage.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.md_5.bungee.api.ChatColor;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwater.storage.data.Creator;
import pl.blackwater.storage.data.StorageRent;
import pl.blackwater.storage.inventories.StorageRentInventory;
import pl.blackwater.storage.managers.CreatorManager;
import pl.blackwater.storage.managers.StorageRentManager;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.storage.StorageBuyPacket;
import pl.justsectors.packets.impl.storage.StorageCreatePacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;

public class PlayerInteractListener implements Listener,Colors{
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		final Creator c = CreatorManager.getCreator(p);
		if(c == null) return;
		final Action a = e.getAction();
		final ItemStack item = e.getItem();
		if(a.equals(Action.LEFT_CLICK_BLOCK) && item != null && item.getType().equals(Material.WOOD_AXE)){
			c.setLeftClick(true);
		}else if(a.equals(Action.RIGHT_CLICK_BLOCK) && item != null && item.getType().equals(Material.WOOD_AXE)){
			c.setRightClick(true);
		}
		if(c.isLeftClick() && c.isRightClick() && !c.isRegion()) {
			Bukkit.dispatchCommand(p, "rg define storage-" + (StorageRentManager.getStoragerents().size() + 1));
			ProtectedRegion region = Core.getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("storage-" + (StorageRentManager.getStoragerents().size() + 1));
			assert region != null;
			StorageRentManager.addFlagstoRegion(region);
			c.setRegion(true);
			c.setStorageRegion(region);
			Util.sendMsg(p, Util.replaceString(SpecialSigns + "->> " + WarningColor + "Utworzono region " + region.getId()));
		}
		if(a.equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getClickedBlock().getType().equals(Material.WALL_SIGN) || e.getClickedBlock().getType().equals(Material.SIGN)) {
				c.setSign((Sign)e.getClickedBlock().getState());
				Util.sendMsg(p, Util.replaceString(SpecialSigns + "->> " + WarningColor + "Ustawiono lokacje SIGN"));
			}
		}
		if(c.getInteractBlock() == null) {
			if(a.equals(Action.RIGHT_CLICK_BLOCK)) {
				if(e.getClickedBlock().getType().equals(Material.EMERALD_BLOCK)) {
					c.setInteractBlock(e.getClickedBlock());
					Util.sendMsg(p, Util.replaceString(SpecialSigns + "->> " + WarningColor + "Ustawiono lokacje interactBlock"));
				}
			}
		}
		if(c.getSign() != null && c.isRegion() && c.getInteractBlock() != null) {
			StorageRent storage = StorageRentManager.addStorageRent(c.getSign(), c.getRentTime(), c.getCost(), c.getStorageRegion(), c.getInteractBlock());
			storage.insert();
			Util.sendMsg(p, Util.replaceString(SpecialSigns + "->> " + MainColor + "Utworzono nowy magazyn o id " + ImportantColor + storage.getId()));
			storage.getSign().setLine(0, Util.fixColor(Util.replaceString("->> " + MainColor + "DO KUPNA")));
			storage.getSign().setLine(1, Util.fixColor(Util.replaceString("->> " + MainColor + "ID: " + SpecialSigns + storage.getId())));
			storage.getSign().setLine(2, Util.fixColor(Util.replaceString("->> " + MainColor + "Koszt: " + SpecialSigns + storage.getCost() + "$")));
			storage.getSign().setLine(3, Util.fixColor(Util.replaceString("->> " + MainColor + "Na czas: " + SpecialSigns + Util.secondsToString((int)(storage.getRentTime() / 1000)))));
			storage.getSign().update();
			CreatorManager.removeCreator(p);

			final StorageCreatePacket packet = new StorageCreatePacket(LocationUtil.convertLocationBlockToString(c.getSign().getLocation()),c.getStorageRegion().getId(),LocationUtil.convertLocationBlockToString(c.getInteractBlock().getLocation()),c.getRentTime(),c.getCost());
			RedisClient.sendSectorsPacket(packet);
		}
	}
	@EventHandler
	public void onPlayerSignInteract(PlayerInteractEvent e) {
		final Block b = e.getClickedBlock();
		final Action a = e.getAction();
		if (SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)) {
			if (a.equals(Action.RIGHT_CLICK_BLOCK)) {
				if (b.getType().equals(Material.SIGN) || b.getType().equals(Material.WALL_SIGN)) {
					Sign sign = (Sign) b.getState();
					final StorageRent storage = StorageRentManager.getStorageRent(sign);
					final Player p = e.getPlayer();
					final User u = UserManager.getUser(p);
					if (storage != null && !storage.isRent()) {
						if (u.getMoney() >= storage.getCost()) {

							final StorageBuyPacket packet = new StorageBuyPacket(u.getUuid(), storage.getId());
							RedisClient.sendSectorsPacket(packet);
							ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(MainColor + "Zakupiono magazyn " + ChatColor.GREEN + "%V%")));
						} else
							Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wystarczajacej liczby monet");
					} else {
						if (storage != null && storage.getOwner().equals(u.getUuid())) {
							StorageRentInventory.openBoughtStorage(storage, u, p);
						}
					}
				}
			}
		}
	}
}
