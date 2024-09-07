package pl.blackwater.bossx.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import pl.blackwater.bossx.settings.BossConfig;
import pl.blackwaterapi.utils.Util;

public class InventoryClickListener implements Listener{

	@EventHandler
	public void onClick(InventoryClickEvent e){
        if (Util.fixColor(BossConfig.guiname).equalsIgnoreCase(e.getInventory().getName())) {
            e.setCancelled(true);
        }
	}
}
