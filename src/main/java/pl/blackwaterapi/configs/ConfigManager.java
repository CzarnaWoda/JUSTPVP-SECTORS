package pl.blackwaterapi.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import pl.blackwaterapi.utils.Logger;

public class ConfigManager {
	@Getter public static HashMap<String, ConfigCreator> configs;
	@Getter public static List<String> configslist = new ArrayList<>();
	static{
		configs = new HashMap<>();
	}
	public static void register(ConfigCreator c){
		ConfigCreator cfg = configs.get(c.getConfigName());
		if(cfg == null){
			configs.put(c.getConfigName(), c);
			c.saveDefaultConfig();
			Logger.info("Config " + c.getConfigName() + " was registered");
			configslist.add(c.getConfigName());
		}
	}
	public static ConfigCreator getConfig(String cfgname){
		return configs.get(cfgname);
	}
}
