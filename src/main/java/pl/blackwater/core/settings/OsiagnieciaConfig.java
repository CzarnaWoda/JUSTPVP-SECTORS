package pl.blackwater.core.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.Osiagniecie;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.utils.Util;

public class OsiagnieciaConfig extends ConfigCreator{
	
	public static List<Osiagniecie> BREAK_STONE,EAT_KOX,KILLS,OPEN_CHEST,THROW_PEARL,EAT_REFIL,SPEND_TIME;
	public static List<Integer> BREAK_STONE_AMOUNT,EAT_KOX_AMOUNT,KILLS_AMOUNT,OPEN_CHEST_AMOUNT,THROW_PEARL_AMOUNT,EAT_REFIL_AMOUNT,SPEND_TIME_AMOUNT;
    public static Map<Integer,Osiagniecie> osiagnieciastorage;
    public static List<String> MESSAGE_OSIAGNIECIE;
    public static String MESSAGE_COMPOSITION_TEXT,MESSAGE_COMPOSITION_COMMAND,MESSAGE_COMPOSITION_HIDETEXT,MESSAGE_TITLESEND_TITLE,MESSAGE_TITLESEND_SUBTITLE,GUI_INVENTORYNAME,GUI_ITEMNAME_BREAKSTONE,GUI_ITEMNAME_EATKOX,GUI_ITEMNAME_KILLS,GUI_ITEMNAME_OPENCHEST,GUI_ITEMNAME_THROWPEARL,GUI_ITEMNAME_EATREFIL,GUI_ITEMNAME_SPENDTIME;

	public OsiagnieciaConfig() {
		super("osiagniecia.yml", "osiagniecia config", Core.getPlugin());
    	FileConfiguration config = getConfig();
    	osiagnieciastorage = new HashMap<>();
    	BREAK_STONE = new ArrayList<>();
    	EAT_KOX = new ArrayList<>();
    	KILLS = new ArrayList<>();
    	OPEN_CHEST = new ArrayList<>();
    	THROW_PEARL = new ArrayList<>();
    	EAT_REFIL = new ArrayList<>();
    	SPEND_TIME = new ArrayList<>();
    	BREAK_STONE_AMOUNT = new ArrayList<>();
    	EAT_KOX_AMOUNT = new ArrayList<>();
    	KILLS_AMOUNT = new ArrayList<>();
    	OPEN_CHEST_AMOUNT = new ArrayList<>();
    	THROW_PEARL_AMOUNT = new ArrayList<>();
    	EAT_REFIL_AMOUNT = new ArrayList<>();
    	SPEND_TIME_AMOUNT = new ArrayList<>();
    	MESSAGE_OSIAGNIECIE = config.getStringList("message.osiagniecie");
    	MESSAGE_COMPOSITION_TEXT = Util.replaceString(config.getString("message.composition.text"));
    	MESSAGE_COMPOSITION_COMMAND = Util.replaceString(config.getString("message.composition.command"));
    	MESSAGE_COMPOSITION_HIDETEXT = Util.replaceString(config.getString("message.composition.hidetext"));
    	MESSAGE_TITLESEND_TITLE = Util.replaceString(config.getString("message.titlesend.title"));
    	MESSAGE_TITLESEND_SUBTITLE = Util.replaceString(config.getString("message.titlesend.subtitle"));
    	GUI_INVENTORYNAME = Util.replaceString(config.getString("gui.inventoryname"));
    	GUI_ITEMNAME_BREAKSTONE = Util.replaceString(config.getString("gui.itemname.breakstone"));
    	GUI_ITEMNAME_EATKOX = Util.replaceString(config.getString("gui.itemname.eatkox"));
    	GUI_ITEMNAME_KILLS = Util.replaceString(config.getString("gui.itemname.kills"));
    	GUI_ITEMNAME_OPENCHEST = Util.replaceString(config.getString("gui.itemname.openchest"));
    	GUI_ITEMNAME_THROWPEARL = Util.replaceString(config.getString("gui.itemname.throwpearl"));
    	GUI_ITEMNAME_EATREFIL = Util.replaceString(config.getString("gui.itemname.eatrefil"));
    	GUI_ITEMNAME_SPENDTIME = Util.replaceString(config.getString("gui.itemname.spendtime"));
    	int index = 0;
    	for(String key : config.getConfigurationSection("osiagniecia").getKeys(false)){
    		final ConfigurationSection section = config.getConfigurationSection("osiagniecia." + key);
    		final String name = section.getString("name");
    		final Material m = Material.matchMaterial(section.getString("gui_item"));
    		final ItemStack gui_item = new ItemStack(m);
    		final int gui_itemdata = section.getInt("gui_itemdata");
    		final String gui_itemname = section.getString("gui_itemname");
    		final List<String> gui_itemlore = section.getStringList("gui_itemlore");
    		final List<String> gui_seconditemlore = section.getStringList("gui_seconditemlore");
    		final String type = section.getString("osiagniecietype");
    		final int amount_toachivment = section.getInt("amount_toachivment");
    		final List<String> rewards = section.getStringList("rewardslist");
    		final List<ItemStack> rewardslist = new ArrayList<>();
    		for(String s : rewards){
    			final Material mat = Material.matchMaterial(s);
    			final ItemStack item = new ItemStack(mat);
    			rewardslist.add(item);
    		}
    		final List<Integer> rewardsdata = section.getIntegerList("rewardsdata");
    		final List<Integer> rewardsamount = section.getIntegerList("rewardsamount");
    		final List<String> rewardsenchantmentlevel = section.getStringList("rewardsenchantmentlevel");
    		final List<String> rewardsenchantments = section.getStringList("rewardsenchantment");
    		final int reward_money = section.getInt("rewardmoney");
    		final List<String> reward_keys = section.getStringList("reward_keys");
    		final List<Integer> reward_keysamount = section.getIntegerList("reward_keysamount");
    		final Osiagniecie storage = new Osiagniecie(key, name, gui_item, gui_itemdata, gui_itemname, gui_itemlore, gui_seconditemlore, type, amount_toachivment, rewardslist, rewardsdata, rewardsamount, rewardsenchantments, rewardsenchantmentlevel, reward_money, reward_keys, reward_keysamount);
    		osiagnieciastorage.put(index, storage);
    		if(type.equalsIgnoreCase("break_stone")) {
    			BREAK_STONE.add(storage);
    			BREAK_STONE_AMOUNT.add(amount_toachivment);
    		}
    		else if (type.equalsIgnoreCase("eat_kox")) {
    			EAT_KOX.add(storage);
    			EAT_KOX_AMOUNT.add(amount_toachivment);
    		}
    		else if (type.equalsIgnoreCase("kills")) {
    			KILLS.add(storage);
    			KILLS_AMOUNT.add(amount_toachivment);
    		}
    		else if (type.equalsIgnoreCase("open_chest")) {
    			OPEN_CHEST.add(storage);
    			OPEN_CHEST_AMOUNT.add(amount_toachivment);
    		}
    		else if (type.equalsIgnoreCase("THROW_PEARL")) {
    			THROW_PEARL.add(storage);
    			THROW_PEARL_AMOUNT.add(amount_toachivment);
    		}
    		else if (type.equalsIgnoreCase("eat_refil")) {
    			EAT_REFIL.add(storage);
    			EAT_REFIL_AMOUNT.add(amount_toachivment);
    		}else if (type.equalsIgnoreCase("spend_time")) {
				SPEND_TIME.add(storage);
				SPEND_TIME_AMOUNT.add(amount_toachivment);
			}
    		index++;
    	}
	}

}
