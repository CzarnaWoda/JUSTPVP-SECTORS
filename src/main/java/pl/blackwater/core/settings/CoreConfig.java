package pl.blackwater.core.settings;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import pl.blackwater.core.Core;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.configs.ConfigCreator;

import java.util.ArrayList;
import java.util.List;
public class CoreConfig extends ConfigCreator{
	public static String CURRENT_SECTOR_NAME;
	public static String REDIS_SERVER, REDIS_PASSWORD;
	public static int PROTECTIONMANAGER_PROTECTIONTIME,LIMITMANAGER_SNOWBALL,LIMITMANAGER_ARROWS,SLOTMANAGER_SLOTS,AUTOMESSAGE_INTERVAL,CLEARLAG_INTERVAL,ESCAPE_TIME,CHATMANAGER_SLOWMODE,KIT_VIP_TIME,KIT_SVIP_TIME,KIT_JEDZENIE_TIME,KIT_ADMIN_TIME, LIMITMANAGER_KOX,LIMITMANAGER_REF,LIMITMANAGER_PEARL,DROPMANAGER_MONEY_DEFAULT,DROPMANAGER_MONEY_VIP,DROPMANAGER_MONEY_SVIP,DROPMANAGER_LEVEL_DEFAULT,DROPMANAGER_LEVEL_VIP,DROPMANAGER_LEVEL_SVIP, DROPMANAGER_DIAMOND_DEFAULT,DROPMANAGER_DIAMOND_VIP,DROPMANAGER_DIAMOND_SVIP,DROPMANAGER_KEY_CHANCE_DEFAULT,DROPMANAGER_KEY_CHANCE_VIP,DROPMANAGER_KEY_CHANCE_SVIP,CHATMANAGER_KILLS, DROPMANAGER_EXP_MIN,DROPMANAGER_EXP_MAX, LEVELMANAGER_DIAMONDLEVEL,ANTYMACROMANAGER_CPSAMOUNT,REPAIRMANAGER_MONEY_SVIP,REPAIRMANAGER_MONEY_VIP,REPAIRMANAGER_MONEY_DEFAULT,REPAIRMANAGER_LEVEL_SVIP,REPAIRMANAGER_LEVEL_VIP,REPAIRMANAGER_LEVEL_DEFAULT;
	public static List<Integer> LEVELMANAGER_BROADCASTATLEVEL,KILLSTREAKMANAGER_BROADCASTKILLSTREAK;
	public static String CHATMANAGER_FORMAT_GLOBAL,CHATMANAGER_FORMAT_AGLOBAL,CHATMANAGER_FORMAT_GUILD;
	public static boolean KIT_VIP_STATUS,KIT_SVIP_STATUS,KIT_JEDZENIE_STATUS,KIT_ADMIN_STATUS,ENABLEMANAGER_PUNCH_STATUS,ENABLEMANAGER_STRENGHT,ENABLEMANAGER_PUNCH_COOLDOWN,ENABLEMANAGER_SAFE;
	public static long CREATEMANAGER_ENCHANT_TIME,ENABLEMANAGER_PUNCH_DELAY,ENABLEMANAGER_ENDERPEARL_DELAY;
	public static List<String> COMBATMANAGER_BLOCKEDCMD, SPAWNMANAGER_SPAWNLIST;
	public static double SLOTMANAGER_MULTIPLER;
	public static boolean TELEPORTMANAGER_RANDOMWARPONJOIN;
	public static boolean TELEPORTMANAGER_RANDOMSPAWNONJOIN;
	public static double TREASUREMANAGER_POWER;
	public static boolean ENABLEMANAGER_FISHINGROD, ENABLEMANAGER_SNOWBALL;
	public static long CREATEMANAGER_DIAMOND_TIME;
	public CoreConfig() {
		super("coreconfig.yml", "Core Config", Core.getPlugin());
		FileConfiguration config = getConfig();
		CURRENT_SECTOR_NAME = config.getString("sectors.currentsectorname");
		REDIS_SERVER = config.getString("sectors.redis.server");
		REDIS_PASSWORD = config.getString("sectors.redis.password");
		PROTECTIONMANAGER_PROTECTIONTIME = config.getInt("protectionmanager.protectiontime");
		SLOTMANAGER_SLOTS = config.getInt("slotmanager.slots");
		AUTOMESSAGE_INTERVAL = config.getInt("automessage.interval");
		CLEARLAG_INTERVAL = config.getInt("clearlag.interval");
		ESCAPE_TIME = config.getInt("escape.time");
		CHATMANAGER_SLOWMODE = config.getInt("chatmanager.slowmode");
		KIT_VIP_TIME = config.getInt("kit.vip.time");
		KIT_SVIP_TIME = config.getInt("kit.svip.time");
		KIT_JEDZENIE_TIME = config.getInt("kit.jedzenie.time");
		KIT_ADMIN_TIME = config.getInt("kit.admin.time");
		LIMITMANAGER_KOX = config.getInt("limitmanager.kox");
		LIMITMANAGER_REF = config.getInt("limitmanager.ref");
		LIMITMANAGER_PEARL = config.getInt("limitmanager.pearl");
		LIMITMANAGER_SNOWBALL = config.getInt("limitmanager.snowball");
		DROPMANAGER_MONEY_DEFAULT = config.getInt("dropmanager.money.default");
		DROPMANAGER_MONEY_VIP = config.getInt("dropmanager.money.vip");
		DROPMANAGER_MONEY_SVIP = config.getInt("dropmanager.money.svip");
		DROPMANAGER_LEVEL_DEFAULT = config.getInt("dropmanager.level.default");
		DROPMANAGER_LEVEL_VIP = config.getInt("dropmanager.level.vip");
		DROPMANAGER_LEVEL_SVIP = config.getInt("dropmanager.level.svip");
		DROPMANAGER_DIAMOND_DEFAULT =  config.getInt("dropmanager.diamond.default");
		DROPMANAGER_DIAMOND_VIP =  config.getInt("dropmanager.diamond.vip");
		DROPMANAGER_DIAMOND_SVIP =  config.getInt("dropmanager.diamond.svip");
		DROPMANAGER_KEY_CHANCE_DEFAULT = config.getInt("dropmanager.key.chance.default");
		DROPMANAGER_KEY_CHANCE_VIP = config.getInt("dropmanager.key.chance.vip");
		DROPMANAGER_KEY_CHANCE_SVIP = config.getInt("dropmanager.key.chance.svip");
		CHATMANAGER_KILLS = config.getInt("chatmanager.kills");
		DROPMANAGER_EXP_MIN =  config.getInt("dropmanager.exp.min");
		DROPMANAGER_EXP_MAX =  config.getInt("dropmanager.exp.max");
		LEVELMANAGER_DIAMONDLEVEL = config.getInt("levemanager.diamondlevel");
		ANTYMACROMANAGER_CPSAMOUNT = config.getInt("antymacromanager.cpsamount");
		REPAIRMANAGER_LEVEL_DEFAULT = config.getInt("repairmanager.level.default");
		REPAIRMANAGER_LEVEL_VIP = config.getInt("repairmanager.level.vip");
		REPAIRMANAGER_LEVEL_SVIP = config.getInt("repairmanager.level.svip");
		REPAIRMANAGER_MONEY_DEFAULT = config.getInt("repairmanager.money.default");
		REPAIRMANAGER_MONEY_VIP = config.getInt("repairmanager.money.vip");
		REPAIRMANAGER_MONEY_SVIP = config.getInt("repairmanager.money.svip");
		LEVELMANAGER_BROADCASTATLEVEL = config.getIntegerList("levelmanager.broadcastatlevel");
		KILLSTREAKMANAGER_BROADCASTKILLSTREAK = config.getIntegerList("killstreakmanager.broadcastkillstreak");
		CHATMANAGER_FORMAT_GLOBAL = config.getString("chatmanager.format.global");
		CHATMANAGER_FORMAT_AGLOBAL = config.getString("chatmanager.format.aglobal");
		CHATMANAGER_FORMAT_GUILD = config.getString("chatmanager.format.guild");
		KIT_VIP_STATUS = config.getBoolean("kit.vip.status");
		KIT_SVIP_STATUS = config.getBoolean("kit.svip.status");
		KIT_JEDZENIE_STATUS = config.getBoolean("kit.jedzenie.status");
		KIT_ADMIN_STATUS = config.getBoolean("kit.admin.status");
		ENABLEMANAGER_PUNCH_STATUS = config.getBoolean("enablemanager.punch.status");
		ENABLEMANAGER_STRENGHT = config.getBoolean("enablemanager.strenght");
		ENABLEMANAGER_PUNCH_COOLDOWN =  config.getBoolean("enablemanager.punch.cooldown");
		ENABLEMANAGER_SAFE = config.getBoolean("enablemanager.safe");
		CREATEMANAGER_DIAMOND_TIME = config.getLong("createmanager.diamond.time");
		ENABLEMANAGER_PUNCH_DELAY = config.getLong("enablemanager.punch.delay");
		CREATEMANAGER_ENCHANT_TIME = config.getLong("createmanager.enchant.time");
		ENABLEMANAGER_ENDERPEARL_DELAY = config.getLong("enablemanager.enderpearl.delay");
		COMBATMANAGER_BLOCKEDCMD = config.getStringList("combatmanager.blockedcmd");
		SPAWNMANAGER_SPAWNLIST = config.getStringList("spawnmanager.spawnlist");
		SLOTMANAGER_MULTIPLER = config.getDouble("slotmanager.multipler");
		TELEPORTMANAGER_RANDOMWARPONJOIN = config.getBoolean("teleportmanager.randomwarponjoin");
		TELEPORTMANAGER_RANDOMSPAWNONJOIN = config.getBoolean("teleportmanager.randomspawnonjoin");
		TREASUREMANAGER_POWER = config.getDouble("treasuremanager.power");
		ENABLEMANAGER_FISHINGROD = config.getBoolean("enablemanager.fishingrod");
		ENABLEMANAGER_SNOWBALL = config.getBoolean("enablemanager.snowball");
		LIMITMANAGER_ARROWS = config.getInt("limitmanager.arrows");

	}
	public static int getFakePlayersOnline()
	{
		return (int)SLOTMANAGER_MULTIPLER * Bukkit.getOnlinePlayers().size();
	}
	public static List<Location> getSpawnLocations(){
		final List<Location> locations = new ArrayList<>();
		for(final String s : SPAWNMANAGER_SPAWNLIST)
			locations.add(LocationUtil.convertStringToLocation(s));
		return locations;
	}
	public static void setDiamondCreateTime(final long time) {
		CoreConfig config = new CoreConfig();
		config.setField("createmanager.diamond.time", time);
		new CoreConfig();
	}
}