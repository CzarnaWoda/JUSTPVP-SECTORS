package pl.blackwater.storage.settings;

import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;

public class StorageConfig extends ConfigCreator{
	public static int STORAGE_DAYCOST,STORAGE_MAXEXPIRETIME,STORAGE_ADDMEMBERCOST;
	public StorageConfig() {
		super("storageconfig.yml", "config", Core.getPlugin());
		STORAGE_DAYCOST = getConfig().getInt("storage.daycost");
		STORAGE_MAXEXPIRETIME = getConfig().getInt("storage.maxexpiretime");
		STORAGE_ADDMEMBERCOST = getConfig().getInt("storage.addmembercost");
		
	}

}
