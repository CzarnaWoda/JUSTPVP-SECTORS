package pl.blackwater.core.settings;

import org.bukkit.configuration.file.FileConfiguration;
import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;

import java.util.List;
import java.util.UUID;

public class FanPageConfig extends ConfigCreator {

    public static List<String> uuidlist;
    public FanPageConfig() {
        super("fanpage.yml", "", Core.getPlugin());

        FileConfiguration configuration = getConfig();
        uuidlist = configuration.getStringList("config.uuidlist");

    }

    public void addUUIDtoList(UUID uuid){
        addToListField("config.uuidlist",uuid.toString());
    }
}
