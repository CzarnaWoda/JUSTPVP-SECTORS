package pl.blackwater.core.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.utils.Util;

public class CraftItemListener implements Listener,Colors{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCraftItem(CraftItemEvent e) {
		final ItemStack result = e.getRecipe().getResult();
		final Material type = result.getType();
		if(type == Material.DIAMOND_AXE || type == Material.DIAMOND_SWORD || type == Material.DIAMOND_HELMET || type == Material.DIAMOND_CHESTPLATE ||  type == Material.DIAMOND_LEGGINGS || type == Material.DIAMOND_BOOTS) {
			Util.sendMsg(e.getWhoClicked(), WarningColor + "Blad: " + WarningColor_2 + "Diamentowe itemy sa wylaczone !");
			e.setCancelled(true);
		}
	}

}
