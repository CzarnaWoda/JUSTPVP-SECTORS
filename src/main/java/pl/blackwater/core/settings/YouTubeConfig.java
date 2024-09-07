package pl.blackwater.core.settings;

import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;

public class YouTubeConfig extends ConfigCreator {
    public YouTubeConfig() {
        super("youtube.yml", "YouTube", Core.getPlugin());


    }
     public boolean isSection(String id){
        if(getConfig().getConfigurationSection(id) == null){
            return false;
        }
        return true;
     }
     public void createSection(String id, String playerName){
         ConfigurationSection section = getConfig().createSection(id);
         section.createSection("name");
         section.set("name", playerName);
     }
}
