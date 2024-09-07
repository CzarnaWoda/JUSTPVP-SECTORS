package pl.blackwater.core.settings;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;

public class KitsConfig extends ConfigCreator
{
	public KitsConfig()
	{
		super("kits.yml", "kits config", Core.getPlugin());
		FileConfiguration config = getConfig();
		KIT_VIPITEMS = config.getStringList("kit.vipitems");
		KIT_SVIPITEMS = config.getStringList("kit.svipitems");
		KIT_MVIPITEMS = config.getStringList("kit.mvipitems");
		KIT_JEDZENIEITEMS = config.getStringList("kit.jedzenieitems");
		KIT_ADMINITEMS = config.getStringList("kit.adminitems");

	}
	public static List<String> KIT_VIPITEMS;
	public static List<String> KIT_SVIPITEMS;
	public static List<String> KIT_MVIPITEMS;
	public static List<String> KIT_JEDZENIEITEMS;
	public static List<String> KIT_ADMINITEMS;
}
