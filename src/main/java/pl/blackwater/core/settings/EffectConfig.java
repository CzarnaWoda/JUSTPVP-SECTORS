package pl.blackwater.core.settings;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Effect;
import pl.blackwaterapi.configs.ConfigCreator;

import java.util.HashMap;
import java.util.List;

public class EffectConfig extends ConfigCreator {

    @Getter private static HashMap<String, Effect> effectHashMap;

    public EffectConfig() {
        super("effect.yml", "Effect config", Core.getPlugin());
        effectHashMap = new HashMap<>();

        FileConfiguration configuration = getConfig();

        for(String key : configuration.getConfigurationSection("effects").getKeys(false)){
            ConfigurationSection configurationSection = getConfigurationSection("effects." + key);

            final String name = configurationSection.getString("name");
            final List<String> lore = configurationSection.getStringList("lore");
            final int cost = configurationSection.getInt("cost");
            final PotionEffect potionEffect = new PotionEffect(PotionEffectType.getByName(configurationSection.getString("potionEffect")), configurationSection.getInt("duration"), configurationSection.getInt("amplifer"));
            final Material material = Material.getMaterial(configurationSection.getString("material"));
            final Effect effect = new Effect(name, lore, material, cost, potionEffect);
            effectHashMap.put(key, effect);
        }
    }

}
