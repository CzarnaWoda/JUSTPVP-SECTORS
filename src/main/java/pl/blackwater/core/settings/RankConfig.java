package pl.blackwater.core.settings;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.configs.ConfigCreator;

import java.util.ArrayList;
import java.util.List;

public class RankConfig extends ConfigCreator {

    public static String DEFAULT_RANK;

    public RankConfig() {
        super("ranks.yml", "Ranks and permissions", Core.getPlugin());

        DEFAULT_RANK = getConfig().getString("rank.default");
    }
    public void loadRanks(){
        FileConfiguration config = getConfig();

        for(String key : config.getConfigurationSection("ranks").getKeys(false)){
            ConfigurationSection section = getConfigurationSection("ranks." + key);

            final List<String> permissions = section.getStringList("permissions");
            final List<User> users = new ArrayList<>();
            for(String string : section.getStringList("users")){
                if(UserManager.getUser(string) != null) {
                    users.add(UserManager.getUser(string));
                }
            }
            final String prefix = section.getString("prefix");
            final String suffix = section.getString("suffix");
            Core.getRankManager().getRanks().put(key, new Rank(key, prefix, suffix, permissions));
        }
        Core.sendSystemMessage("Loaded " + Core.getRankManager().getRanks().size() + " ranks!");
    }
}
