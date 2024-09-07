package pl.blackwater.bossx.settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.Core;
import pl.blackwater.core.managers.EnchantManager;
import pl.blackwaterapi.configs.ConfigCreator;

public class RewardsConfig extends ConfigCreator {
		    public static Map<Integer,RewardsStorage> rewardsStroage;

			public RewardsConfig(){
				super("bossrewards.yml","rewards", Core.getPlugin());
		    	int index = 0;
		    	FileConfiguration config = getConfig();
		    	rewardsStroage = new HashMap<Integer, RewardsStorage>();
		    	for(String key : config.getConfigurationSection("rewards").getKeys(false)){
		    		ConfigurationSection section = config.getConfigurationSection("rewards." + key);
		    		
		    		String name = section.getString("name");
		    		Material mat = Material.matchMaterial(section.getString("item"));
		    		ItemStack item = new ItemStack(mat);
		    		int amount = section.getInt("amount");
		    		int data = section.getInt("data");
		    		List<String> enchantmentstring = section.getStringList("enchantments");
		    		List<Enchantment> enchantments = new ArrayList<Enchantment>();
		    		for(int i = 0;i<enchantmentstring.size();i++){
		    			enchantments.add(EnchantManager.get(enchantmentstring.get(i)));
		    		}
		    		List<Integer> enchantmentslevel = section.getIntegerList("enchantlevel");
		    		
		    		RewardsStorage rewards_storage = new RewardsStorage(name, item, amount, data,enchantments,enchantmentslevel);
		    		rewardsStroage.put(index, rewards_storage);
		    		index++;
		    	}
		    }

	}
