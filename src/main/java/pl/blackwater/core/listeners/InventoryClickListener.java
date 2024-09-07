package pl.blackwater.core.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.ItemManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.utils.Util;

public class InventoryClickListener implements Listener,Colors{
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAnvilInventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getName().equalsIgnoreCase(Util.fixColor("&6ChestPvP &8- &cOtwieranie..."))) {
			e.setCancelled(true);
			e.getWhoClicked().sendMessage("§4Blad: §cNie mozesz tego zrobic!");
			return;
		}
		if (e.getInventory().getName().equalsIgnoreCase(Util.fixColor("&6Podglad"))) {
			e.setCancelled(true);
			e.getWhoClicked().sendMessage("§4Blad: §cNie mozesz tego zrobic!");
			return;
		}
		if(e.getView().getType() ==  InventoryType.ANVIL) {
			int rawSlot = e.getRawSlot();
			if(rawSlot == 2) {
				final ItemStack item = e.getCurrentItem();
				if(item != null) {
					final ItemMeta meta = item.getItemMeta();
					if(meta != null) {
						/*if(meta.getEnchants().get(Enchantment.DURABILITY) != null) {
							if(meta.getEnchants().get(Enchantment.DURABILITY) > 2) {
								Util.sendMsg(e.getWhoClicked(), WarningColor + "Blad: " + WarningColor_2 + "Zaklecie UNBREAKING-3 jest zablokowane na serwerze!");
								meta.removeEnchant(Enchantment.DURABILITY);
								meta.addEnchant(Enchantment.DURABILITY, 2, true);
								item.setItemMeta(meta);
							}
						}*/
						if(meta.getEnchants().get(Enchantment.ARROW_INFINITE) != null && meta.getEnchants().get(Enchantment.ARROW_KNOCKBACK) != null){
							Util.sendMsg(e.getWhoClicked(), WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz miec zaklecia INFINITY i PUNCH na luku!");
						}
						if(meta.getEnchants().get(Enchantment.DAMAGE_ALL) != null) {
							if (meta.getEnchants().get(Enchantment.DAMAGE_ALL) > 4) {
								Util.sendMsg(e.getWhoClicked(), WarningColor + "Blad: " + WarningColor_2 + "Zaklecie SHARPNESS-5 jest zablokowane na serwerze!");
								meta.removeEnchant(Enchantment.DAMAGE_ALL);
								meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
								item.setItemMeta(meta);
							}
						}
						if(meta.getEnchants().get(Enchantment.ARROW_DAMAGE) != null) {
							if (meta.getEnchants().get(Enchantment.ARROW_DAMAGE) > 3) {
								Util.sendMsg(e.getWhoClicked(), WarningColor + "Blad: " + WarningColor_2 + "Zaklecie POWER-5,POWER-4 jest zablokowane na serwerze!");
								meta.removeEnchant(Enchantment.ARROW_DAMAGE);
								meta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
								item.setItemMeta(meta);
							}
						}
						if(meta.getEnchants().get(Enchantment.FIRE_ASPECT) != null) {
							if (meta.getEnchants().get(Enchantment.FIRE_ASPECT) > 1) {
								Util.sendMsg(e.getWhoClicked(), WarningColor + "Blad: " + WarningColor_2 + "Zaklecie FIRE_ASPECT-2 jest zablokowane na serwerze!");
								meta.removeEnchant(Enchantment.FIRE_ASPECT);
								meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
								item.setItemMeta(meta);
							}
						}
						if(meta.getEnchants().get(Enchantment.KNOCKBACK) != null){
							Util.sendMsg(e.getWhoClicked(), WarningColor + "Blad: " + WarningColor_2 + "Zaklecie KNOCKBACK jest zablokowane na serwerze!");
							meta.removeEnchant(Enchantment.KNOCKBACK);
							item.setItemMeta(meta);
						}
					}
				}
			}
		}
	}
}
