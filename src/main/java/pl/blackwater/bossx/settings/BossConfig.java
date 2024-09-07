package pl.blackwater.bossx.settings;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;

public class BossConfig extends ConfigCreator {
	//configuracje
	public static String bossname;
	public static int bosshealth;
	public static List<PotionEffectType> bosseffect;
	public static boolean broadcastenable;
	public static String broadcastmessage;
	public static boolean soundenable;
	public static String messageconsole;
	public static String messageprefix;
	public static String bossdeathmessage;
	public static String bosshealthactionbar;
	public static String reloadmessage;
	public static String guiname;
	@SuppressWarnings("unchecked")
	public BossConfig()
	{
		super("bossconfig.yml","boss",Core.getPlugin());
		FileConfiguration config = getConfig();
		bossname = config.getString("boss.name").replace(">>", "»").replace("<<", "«").replace("*", "•");
		bosshealth = config.getInt("boss.health");
		bosshealthactionbar = config.getString("boss.message.actionbar").replace(">>", "»").replace("<<", "«").replace("*", "•");
		bosseffect = (List<PotionEffectType>) config.get("boss.effect");
		broadcastenable = config.getBoolean("broadcast.enable");
		broadcastmessage = config.getString("broadcast.message").replace(">>", "»").replace("<<", "«").replace("*", "•");
		soundenable = config.getBoolean("sound.enable");
		messageconsole = config.getString("console.message").replace(">>", "»").replace("<<", "«").replace("*", "•");
		messageprefix = config.getString("message.prefix").replace(">>", "»").replace("<<", "«").replace("*", "•");
		bossdeathmessage = config.getString("message.death").replace(">>", "»").replace("<<", "«").replace("*", "•");
		reloadmessage = config.getString("message.reload").replace(">>", "»").replace("<<", "«").replace("*", "•");
		guiname = config.getString("gui.name").replace(">>", "»").replace("<<", "«").replace("*", "•");
	}
}
