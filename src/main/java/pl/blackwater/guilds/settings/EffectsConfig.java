package pl.blackwater.guilds.settings;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.blackwater.core.Core;
import pl.blackwater.guilds.data.GuildEffect;
import pl.blackwaterapi.configs.ConfigCreator;

import java.util.ArrayList;
import java.util.List;

public class EffectsConfig extends ConfigCreator {

    @Getter public static List<GuildEffect> effectHashMap;
    public EffectsConfig() {
        super("guildeffects.yml", "dobry config", Core.getPlugin());
        effectHashMap = new ArrayList<>();

        final FileConfiguration config = getConfig();
        for(String key : config.getConfigurationSection("guildeffects").getKeys(false)){
            ConfigurationSection section = getConfigurationSection("guildeffects." + key);


            PotionEffect potionEffect = new PotionEffect(PotionEffectType.getByName(section.getString("potioneffecttype")), section.getInt("duration") * 20, section.getInt("amplifer"));
            Material material = Material.matchMaterial(section.getString("material"));
            short data = (short)section.getInt("data");

            GuildEffect guildEffect = new GuildEffect(key, material, data, potionEffect);

            effectHashMap.add(guildEffect);

        }

    }
}
