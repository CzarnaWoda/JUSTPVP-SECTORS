package pl.blackwater.core.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.Shops;
import pl.blackwater.core.data.TradeItems;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.utils.Util;

public class ShopConfig extends ConfigCreator{
    public static String buymessage_enoughtmoney,buymessage_finalbuy,sellguiname,tradeguiname,buyguiname,sellmessage_enoughtitem,sellmessage_finalsell,trademessage_enoughtitem,trademessage_finaltrade;
    public static List<String> sellitemlore,levelitemlore,tradeitemlore,buyitemlore;
    public static int tradeguirows,sellguirows,buyguirows;
    public static Map<Integer,Shops.ShopBuyItems> buystorage;
    public static Map<Integer,Shops.ShopSellItems> sellstorage;
    public static Map<Integer,Shops.LevelBuyItems> levelstorage;
    public static Map<Integer,TradeItems> tradestorage;
	public ShopConfig() {
		super("shops.yml", "Sklepy serwer'a", Core.getPlugin());
		   FileConfiguration config = getConfig();
		   buyguiname = Util.replaceString(config.getString("buygui.name"));
		   buyguirows = config.getInt("buygui.rows");
		   tradeguiname = Util.replaceString(config.getString("tradegui.name"));
		   tradeguirows = config.getInt("tradegui.rows");
		   buyitemlore = config.getStringList("buyitem.lore");
		   tradeitemlore = config.getStringList("tradeitems.lore");
		   buymessage_enoughtmoney = config.getString("buy.message.enoughtmoney");
		   buymessage_finalbuy = config.getString("buy.message.finalbuy");
		   sellguiname = Util.replaceString(config.getString("sellgui.name"));
		   sellguirows = config.getInt("sellgui.rows");
		   sellitemlore = config.getStringList("sellitem.lore");
		   sellmessage_enoughtitem = config.getString("sell.message.enoughtitem");
		   sellmessage_finalsell = config.getString("sell.message.finalsell");
		   levelitemlore = config.getStringList("levelitem.lore");
		   trademessage_enoughtitem = config.getString("trade.message.enoughtitem");
		   trademessage_finaltrade = config.getString("trade.message.finaltrade");
		   buystorage = new HashMap<>();
		   sellstorage = new HashMap<>();
		   levelstorage = new HashMap<>();
		   tradestorage = new HashMap<>();
		   int index = 0;
		   for (final String key : config.getConfigurationSection("buymenu").getKeys(false)){
			   final ConfigurationSection section = config.getConfigurationSection("buymenu." + key);
			   
			   final String name = section.getString("name");
			   final int cost = section.getInt("cost");
			   final Material m = Material.matchMaterial(section.getString("item"));
			   final ItemStack item = new ItemStack(m);
			   final int data = section.getInt("data");
			   final int amount = section.getInt("amount");
			   
			   final Shops.ShopBuyItems Buystorage = new Shops.ShopBuyItems(key, name, cost, item, data, amount);
			   buystorage.put(index, Buystorage);
			   index ++;
		   }
		   int index2 = 0;
		   for (final String key : config.getConfigurationSection("sellmenu").getKeys(false)){
			   final ConfigurationSection section = config.getConfigurationSection("sellmenu." + key);
			   
			   final String name = section.getString("name");
			   final int sellmoney = section.getInt("sellmoney");
			   final Material m = Material.matchMaterial(section.getString("item"));
			   final ItemStack item = new ItemStack(m);
			   final int data = section.getInt("data");
			   final int amount = section.getInt("amount");
			   
			   final Shops.ShopSellItems Sellstorage = new Shops.ShopSellItems(key, name, sellmoney, item, data, amount);
			   sellstorage.put(index2, Sellstorage);
			   index2 ++;
		   }
		   int index3 = 0;
		   /*for(final String key : config.getConfigurationSection("buylevelmenu").getKeys(false)){
			   final ConfigurationSection section = config.getConfigurationSection("buylevelmenu." + key);
			   
			   final String name = section.getString("name");
			   final Material m0 = Material.matchMaterial(section.getString("costitem"));
			   final ItemStack item0 = new ItemStack(m0);
			   final int cost = section.getInt("cost");
			   final Material m = Material.matchMaterial(section.getString("item"));
			   final ItemStack item = new ItemStack(m);
			   final int data = section.getInt("data");
			   final int amount = section.getInt("amount");
			   
			   final Shops.LevelBuyItems levelstorages = new Shops.LevelBuyItems(key, name,item0, cost, item, data, amount);
			   levelstorage.put(index3, levelstorages);
			   index3 ++;
		   }*/
		   int index4 = 0;
		   for(final String key : config.getConfigurationSection("tradeitem").getKeys(false)){
			   final ConfigurationSection section = config.getConfigurationSection("tradeitem." + key);
			   
			   final String name = section.getString("name");
			   final Material cost_item = Material.matchMaterial(section.getString("cost_item"));
			   final int cost_amount = section.getInt("cost_amount");
			   final Material item = Material.matchMaterial(section.getString("item"));
			   final int data = section.getInt("data");
			   final int amount = section.getInt("amount");
			   final List<String> enchantments = section.getStringList("enchantments");
			   final List<Integer> enchantmentslevel = section.getIntegerList("enchantmentslevel");
			   
			   tradestorage.put(index4, new TradeItems(key, name, cost_item, cost_amount, item, data, amount, enchantments, enchantmentslevel));
			   index4++;
		   }
	}

}
