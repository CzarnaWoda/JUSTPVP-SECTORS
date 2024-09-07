package pl.blackwater.bossx.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.bossx.settings.BossConfig;
import pl.blackwater.bossx.settings.RewardsConfig;
import pl.blackwater.bossx.settings.RewardsStorage;
import pl.blackwaterapi.utils.Util;

public class GUI {
	
	public static InventoryView OpenMenu(Player p){
		Inventory inv = Bukkit.createInventory(null, 54,Util.fixColor(BossConfig.guiname));
		int i = 0;
		for(RewardsStorage storage : RewardsConfig.rewardsStroage.values()){
			ItemStack item = storage.getWithAll();
			inv.setItem(i, item);
			i++;
		}
		return p.openInventory(inv);
	}

}
