package pl.blackwater.core.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.utils.Util;

public class BlockStrenghtListener implements Listener,Colors{
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockDispense(BlockDispenseEvent e) {
		if(CoreConfig.ENABLEMANAGER_STRENGHT)
			return;
		final ItemStack item = e.getItem();
		if(item.getType().equals(Material.POTION) && (item.getDurability() == (short)41 || item.getDurability() == (short)16425 || item.getDurability() == (short)16457 || item.getDurability() == (short)8265 || item.getDurability() == (short)8233 || item.getDurability() == (short)8201)) {
			e.setCancelled(true);
		}
	}
}
