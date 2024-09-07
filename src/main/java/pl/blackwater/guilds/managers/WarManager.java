package pl.blackwater.guilds.managers;

import lombok.Getter;
import pl.blackwater.core.Core;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.War;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.redis.channels.RedisChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarManager {

    @Getter
    public static List<War> wars = new ArrayList<>();

    public static boolean hasWar(Guild g, Guild o)
    {
        for (War w : wars) {
            if (((w.getGuild1().equals(g)) && (w.getGuild2().equals(o))) || ((w.getGuild2().equals(g)) && (w.getGuild1().equals(o)))) {
                return true;
            }
        }
        return false;
    }

    public static War createWar(Guild g, Guild o, long endTime){
        if(hasWar(g,o)){
            return null;
        }
        War w = new War(g,o,endTime);

        wars.add(w);
        return w;
    }



    public static War getWar(Guild g){
        for (War w : wars) {
            if (w.getGuild1().equals(g) || w.getGuild2().equals(g)) {
                return w;
            }
        }
        return null;
    }

    public static War getWar(String guildTag){
        for (War w : wars) {
            if (w.getGuild1().getTag().equalsIgnoreCase(guildTag) || w.getGuild2().getTag().equals(guildTag)) {
                return w;
            }
        }
        return null;
    }

    public static War getWarWithGuild(Guild g, Guild o) {
        for (War w : wars) {
            if (w.getGuild1().equals(g) && w.getGuild2().equals(o) || w.getGuild1().equals(o) && w.getGuild2().equals(g)) {
                return w;
            }
        }
        return null;
    }
    public static void removeWar(War war){
        wars.remove(war);
        war.delete();
    }
    public static void enable()
    {
        for (String warString : RedisChannel.INSTANCE.WARS) {
            final War war = GsonUtil.fromJson(warString, War.class);
            wars.add(war);
        }
        Logger.info("Loaded " + wars.size() + " wars!");
    }
}
