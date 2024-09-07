package pl.blackwater.chestpvpdrop.settings;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.blackwater.core.Core;
import pl.blackwaterapi.utils.Logger;
import pl.blackwaterapi.utils.Util;

public class Config {
	public static double DROP_VIP_DROP;
	public static int DROP_VIP_EXP;
	public static double DROP_TURBO_DROP;
	public static int DROP_TURBO_EXP;
	public static int DROP_TURBO_LOSOWANIE_ILOSC;
	public static String MESSAGE_SKLEP;
	public static int DROP_OBSIDIAN_AMOUNT;
	public static boolean COINS_STATUS;
	public static int COINS_MIN;
	public static int COINS_MAX;
	public static int EVENT_EXP;
	public static int EVENT_DROP;
	public static List<Integer> DROP_LVL_BROADCAST;
	public static String DROP_CHEST_NAME;
	public static long DROP_CHEST_TIME;
    private static File file;
    private static FileConfiguration c;

	static {
        Config.file = new File(Core.getPlugin().getDataFolder(), "dropconfig.yml");
        Config.c = null;
		Config.DROP_CHEST_TIME = 0L;
		Config.DROP_VIP_DROP = 0.5;
		Config.DROP_VIP_EXP = 1;
		Config.DROP_TURBO_DROP = 1;
		Config.DROP_TURBO_EXP = 1;
		Config.DROP_TURBO_LOSOWANIE_ILOSC = 3;
		Config.MESSAGE_SKLEP = "&8� &7Mozliwosc kupna, pod komenda &e/sms";
		Config.COINS_STATUS = false;
		Config.COINS_MIN = 5;
		Config.COINS_MAX = 25;
		Config.EVENT_EXP = 0;
		Config.EVENT_DROP = 0;
		Config.DROP_OBSIDIAN_AMOUNT = 1;
		Config.DROP_CHEST_NAME = "czarnawodacase";
		Config.DROP_LVL_BROADCAST = Arrays.asList(1, 5, 10, 15, 20, 25, 30, 35);
	}
	
    public static void loadConfig() {
        try {
            if (!Config.file.exists()) {
                Config.file.getParentFile().mkdirs();
                final InputStream is = Core.getPlugin().getResource(Config.file.getName());
                if (is != null) {
                    Util.copy(is, Config.file);
                }
            }
            Config.c = YamlConfiguration.loadConfiguration(Config.file);
            Field[] fields;
            for (int length = (fields = Config.class.getFields()).length, i = 0; i < length; ++i) {
                final Field f = fields[i];
                if (Config.c.isSet("dropconfig." + f.getName().toLowerCase().replaceFirst("_", ",").replace(",", "."))) {
                    f.set(null, Config.c.get("dropconfig." + f.getName().toLowerCase().replaceFirst("_", ",").replace(",", ".")));
                }
            }
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }
    
    public static void saveConfig() {
        try {
            Field[] fields;
            for (int length = (fields = Config.class.getFields()).length, i = 0; i < length; ++i) {
                final Field f = fields[i];
                Config.c.set("dropconfig." + f.getName().toLowerCase().replaceFirst("_", ",").replace(",", "."), f.get(null));
            }
            Config.c.save(Config.file);
        }
        catch (Exception e) {
            Logger.exception(e);
        }
    }
    
    public static void reloadConfig() {
        loadConfig();
        saveConfig();
    }
    public static void setDropChestTime(long t)
    {
    	DROP_CHEST_TIME = t;
    	saveConfig();
    }
}
