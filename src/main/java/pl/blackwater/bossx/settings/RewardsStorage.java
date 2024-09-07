package pl.blackwater.bossx.settings;

import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import lombok.Setter;
import pl.blackwaterapi.utils.Util;
@Getter
@Setter
public class RewardsStorage {
	private String name;
	private ItemStack item;
	private int amount;
	private int data;
	private List<Enchantment> enchantments;
	private List<Integer> enchantemntslevel;
	
	public RewardsStorage(String name,ItemStack item,int amount,int data,List<Enchantment> enchantments,List<Integer> enchantemntslevel){
		this.name = name;
		this.item = item;
		this.amount = amount;
		this.data = data;
		this.enchantments = enchantments;
		this.enchantemntslevel = enchantemntslevel;
	}
	public ItemStack getWithAll(){
		ItemStack item_stack = new ItemStack(item.getType(),amount,(short)data);
		ItemMeta meta = item_stack.getItemMeta();
		meta.setDisplayName(Util.fixColor(name));
		if(enchantments.size() > 0){
		for(int i = 0; i<enchantments.size();i++){
			meta.addEnchant(enchantments.get(i), enchantemntslevel.get(i), true);
		}
		}
		item_stack.setItemMeta(meta);
		return item_stack;
	}
}
