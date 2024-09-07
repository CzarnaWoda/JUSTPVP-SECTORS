package pl.blackwater.market.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {
	
	public static String ConvertItemStackToString(ItemStack item){
		String enchants = "";
		if(item.getItemMeta().hasEnchants()){
			for(Enchantment enchant : item.getItemMeta().getEnchants().keySet()){
				enchants = enchants + enchant.getName() + "-" + item.getItemMeta().getEnchants().get(enchant) + ";";
			}
		}
		String itemStack = item.getType().toString() + "," + item.getAmount() + "," + item.getDurability() + "," + enchants;
		return itemStack;
	}
	public static ItemStack ConvertStringToItemStack(String item){
		String[] split = item.split(",");
		ItemStack itemStack = new ItemStack(Material.getMaterial(split[0]));
		itemStack.setAmount(Integer.valueOf(split[1]));
		itemStack.setDurability(Short.valueOf(split[2]));
		if(split.length >= 4){
		String enchants = split[3];
		String[] enchantss = enchants.split(";");
		ItemMeta meta = itemStack.getItemMeta();
		for(String s : enchantss){
			String[] true_enchant = s.split("-");
			meta.addEnchant(Enchantment.getByName(true_enchant[0]), Integer.valueOf(true_enchant[1]), true);
		}
		itemStack.setItemMeta(meta);
		}
		return itemStack;
	}

}
