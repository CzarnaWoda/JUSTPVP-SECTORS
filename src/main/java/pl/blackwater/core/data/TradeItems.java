package pl.blackwater.core.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Data;
import pl.blackwater.core.managers.EnchantManager;
import pl.blackwater.core.managers.ItemManager;
import pl.blackwater.core.settings.ShopConfig;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;

@Data
public class TradeItems {
	private int cost_amount,data,amount;
	private String id,name;
	private Material cost_item,item;
	private List<String> enchantments;
	private List<Integer> enchantmentslevel;
	public TradeItems(final String id,final String name,final Material cost_item,final int cost_amount,final Material item,final int data,final int amount,final List<String> enchantments,final List<Integer> enchantmentslevel){
		this.id = id;
		this.name = Util.replaceString(name);
		this.cost_item = cost_item;
		this.cost_amount = cost_amount;
		this.item = item;
		this.data = data;
		this.amount = amount;
		this.enchantments = enchantments;
		this.enchantmentslevel = enchantmentslevel;
	}
	public List<Enchantment> getEnchantments(){
		List<Enchantment> list = new ArrayList<>();
		for(String s : enchantments){
			list.add(EnchantManager.get(s));
		}
		return list;
	}
	public ItemStack getItem_ItemStack(){
		ItemStack itemstack = new ItemStack(item,amount,(short)data);
		ItemMeta meta = itemstack.getItemMeta();
		List<Enchantment> enchantmentlist = getEnchantments();
		List<Integer> levellist = getEnchantmentslevel();
		for(int i = 0; i < enchantmentlist.size();i++){
			meta.addEnchant(enchantmentlist.get(i), (levellist.get(i) == null ? enchantmentlist.get(i).getMaxLevel() : levellist.get(i)), true);
		}
		itemstack.setItemMeta(meta);
		return itemstack;
	}
	public ItemBuilder getItem_ItemBuilder(){
		ItemBuilder itembuilder = new ItemBuilder(item,amount,(short)data)
					.setTitle(getName());
		List<Enchantment> enchantmentlist = getEnchantments();
		List<Integer> levellist = getEnchantmentslevel();
		for(int i = 0; i < enchantmentlist.size();i++){
			itembuilder.addEnchantment(enchantmentlist.get(i), (levellist.get(i) == null ? enchantmentlist.get(i).getMaxLevel() : levellist.get(i)));
		}
		for(String s : ShopConfig.tradeitemlore){
			itembuilder.addLore(Util.fixColor(Util.replaceString(s
						.replace("{COST_AMOUNT}", String.valueOf(getCost_amount()))
						.replace("{COST_ITEM}", "JustCoin"))));
		}
		return itembuilder;
	}
	public ItemStack getCostItem_ItemStack(){
		return new ItemStack(cost_item,cost_amount);
	}
}
