package pl.blackwater.core.utils;

import org.bukkit.Material;

import pl.blackwaterapi.utils.Util;

public class MaterialUtil {
	
	@SuppressWarnings("deprecation")
	public static Material getMaterial(String materialName) {
        Material returnMaterial = null;
        if (Util.isInteger(materialName)) {
            int id = Integer.parseInt(materialName);
            returnMaterial = Material.getMaterial(id);
        }
        else {
            returnMaterial = Material.matchMaterial(materialName);
        }
        return returnMaterial;
    }

}
