package pl.blackwater.core.settings;

import org.bukkit.configuration.file.FileConfiguration;
import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.utils.Util;

import java.util.HashMap;
import java.util.Map;

public class TabListConfig extends ConfigCreator {
    public static Map<Integer,String> playerList;
    public static String TABLIST_HEADER;
    public static String TABLIST_FOOTER;
    public TabListConfig() {
        super("tablist.yml", "tablist", Core.getPlugin());
        final FileConfiguration c = getConfig();
        TABLIST_HEADER = Util.fixColor(Util.replaceString(c.getString("tablist.header")));
        TABLIST_FOOTER = Util.fixColor(Util.replaceString(c.getString("tablist.footer")));

        playerList = new HashMap<>();
        for(int i = 0;i < 80;i ++) {
            if (c.get("tablist." + i) != null){
                playerList.put(i,Util.fixColor(Util.replaceString(c.getString("tablist." + i))));
            }
        }
    }
}
