package pl.blackwater.chestpvpdrop.managers;

import org.bukkit.Material;
import pl.blackwater.chestpvpdrop.data.Drop;
import pl.blackwater.chestpvpdrop.data.DropData;
import pl.blackwater.chestpvpdrop.drops.CancelDropData;
import pl.blackwater.chestpvpdrop.drops.NormalDropData;
import pl.blackwater.chestpvpdrop.drops.RandomDropData;

import java.util.HashMap;

public class DropManager
{
    private static final HashMap<Material, DropData> drops;
    private static final HashMap<Material, Integer> exps;
    
    public static void setup() {
        drops.clear();
        exps.clear();
        for (String s : DropFile.getConfig().getStringList("cancel-drops")) {
            drops.put(Material.getMaterial(s), new CancelDropData());
        }
        RandomDropData data = new RandomDropData();
        for (Drop d : RandomDropData.getDrops()) {
            drops.put(d.getFrom(), data);
        }
        for (String s2 : DropFile.getConfig().getConfigurationSection("exp-drops").getKeys(false)) {
            exps.put(Material.getMaterial(s2), DropFile.getConfig().getInt("exp-drops." + s2, 1));
        }
    }
    
    public static DropData getDropData(final Material mat) {
        DropData drop = new NormalDropData();
        if (DropManager.drops.containsKey(mat)) {
            drop = DropManager.drops.get(mat);
        }
        return drop;
    }
    
    public static int getExp(final Material mat) {
        int exp = 0;
        if (DropManager.exps.containsKey(mat)) {
            exp = DropManager.exps.get(mat);
        }
        return exp;
    }
    
    public static HashMap<Material, DropData> getDrops() {
        return DropManager.drops;
    }
    
    public static HashMap<Material, Integer> getExps() {
        return DropManager.exps;
    }
    
    static {
        drops = new HashMap<>();
        exps = new HashMap<>();
    }
}
