package pl.blackwater.core.settings;

import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;


public class SectorConfig extends ConfigCreator {


    public SectorConfig() {
        super("sectors.yml", "Sectors Config", Core.getPlugin());
        FileConfiguration config = getConfig();
        for (String key : config.getConfigurationSection("sectors").getKeys(false)) {
            final ConfigurationSection section = getConfigurationSection("sectors." + key);
            final SectorType type = SectorType.valueOf(section.getString("type"));
            final Sector sector = new Sector(section.getString("name"), type);
            SectorManager.registerSector(sector);
        }

    }

}
