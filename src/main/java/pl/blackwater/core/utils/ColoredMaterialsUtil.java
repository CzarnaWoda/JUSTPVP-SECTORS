package pl.blackwater.core.utils;

import org.bukkit.Material;

import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;

public class ColoredMaterialsUtil {
	
	public static ItemBuilder getStainedGlassPane(final short s) {
		return new ItemBuilder(Material.STAINED_GLASS_PANE,1,s).setTitle(Util.fixColor("&2"));
	}

}
