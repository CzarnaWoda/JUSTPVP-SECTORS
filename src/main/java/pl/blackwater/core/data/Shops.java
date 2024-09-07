package pl.blackwater.core.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.blackwaterapi.utils.Util;
@SuppressWarnings("ALL")
public class Shops
{
	@Getter public static Shops shop;
	@Getter public static Map<String, Integer> boosts = new HashMap<>();
	static{
		boosts.put("CHEST_1",5000);
		boosts.put("CHEST_2",10000);
		boosts.put("CHEST_3",15000);
		boosts.put("CHEST_4",22000);
		boosts.put("CHEST_5",28000);
		boosts.put("CHEST_6",45000);
		boosts.put("ULTRAMONEY_1", 45000);
		boosts.put("ULTRAMONEY_2", 75000);
		boosts.put("ULTRAMONEY_3", 95000);
		boosts.put("GRUBASEK_1", 20000);
		boosts.put("GRUBASEK_2", 25000);
		boosts.put("GRUBASEK_3", 30000);
		boosts.put("KOXPVP_1", 40000);
		boosts.put("KOXPVP_2", 70000);
		boosts.put("KOXPVP_3", 125000);
		boosts.put("ULTRAEXP_1", 40000);
		boosts.put("ULTRAEXP_2", 60000);
		boosts.put("ULTRAEXP_3", 90000);
		boosts.put("MNIEJSZYGRUBASEK_1", 10000);
		boosts.put("MNIEJSZYGRUBASEK_2", 20000);
		boosts.put("MNIEJSZYGRUBASEK_3", 25000);
	}
	public Shops(){
		shop = this;
	}
@Data
public static class ShopBuyItems {
	private int data,cost,amount;
	private String id,name;
	private ItemStack item;
	public ShopBuyItems(final String id,final String name,final int cost,final ItemStack item,final int data,final int amount){
		this.id = id;
		this.name = Util.replaceString(name);
		this.cost = cost;
		this.item = item;
		this.data = data;
		this.amount = amount;
	}
	public final ItemStack getItemWithAll(){
		return new ItemStack(getItem().getType(),getAmount(),(short)getData());
	}
	
}
@Data
public static class ShopSellItems {
	private int sellmoney,data,amount;
	private String id,name;
	private ItemStack item;
	public ShopSellItems(final String id,final String name,final int sellmoney,final ItemStack item,final int data,final int amount){
		this.id = id;
		this.name = Util.replaceString(name);
		this.sellmoney = sellmoney;
		this.item = item;
		this.data = data;
		this.amount = amount;
	}
	public final ItemStack getItemWithAll(){
		return new ItemStack(getItem().getType(),getAmount(),(short)getData());
	}
}
@Data
public static class LevelBuyItems {
	private String id,name;
	private ItemStack costitem,item;
	private int data,amount,cost;
	public LevelBuyItems(final String id ,final String name,ItemStack costitem,final int cost,final ItemStack item,final int data,final int amount){
		this.id = id;
		this.name = Util.replaceString(name);
		this.costitem = costitem;
		this.cost = cost;
		this.item = item;
		this.data = data;
		this.amount = amount;
	}
	@NotNull
	@Contract(" -> new")
	public final ItemStack getItemWithAll(){
		return new ItemStack(item.getType(),amount,(short)data);
	}
}
}
