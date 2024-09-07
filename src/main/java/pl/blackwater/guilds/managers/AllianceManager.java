package pl.blackwater.guilds.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import pl.blackwater.core.Core;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.scoreboard.*;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.redis.channels.RedisChannel;

public class AllianceManager
{
    public static boolean hasAlliance(Guild g, Guild o)
    {
        for (Alliance a : alliances) {
            if (((a.getGuild1().equals(g)) && (a.getGuild2().equals(o))) || ((a.getGuild2().equals(g)) && (a.getGuild1().equals(o)))) {
                return true;
            }
        }
        return false;
    }

    public static void createAlliance(Guild g, Guild o)
    {
        if (hasAlliance(g, o)) {
            return;
        }
        Alliance a = new Alliance(g, o);
        a.insert();
        alliances.add(a);
    }

    public static void removeAlliance(Guild g, Guild o)
    {
        if (!hasAlliance(g, o)) {
            return;
        }
        Alliance a = getAlliance(g, o);
        assert a != null;
        a.delete();
        ScoreBoardNameTag.updateBoard(o);
        ScoreBoardNameTag.updateBoard(g);
        alliances.remove(a);
    }

    public static Alliance getAlliance(Guild g, Guild o)
    {
        for (Alliance a : alliances) {
            if (((a.getGuild1().equals(g)) && (a.getGuild2().equals(o))) || ((a.getGuild2().equals(g)) && (a.getGuild1().equals(o)))) {
                return a;
            }
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Set<Alliance> getGuildAlliances(Guild g)
    {
        Set<Alliance> all = new HashSet();
        for (Alliance a : alliances) {
            if ((a.getGuild1().equals(g)) || (a.getGuild2().equals(g))) {
                all.add(a);
            }
        }
        return all;
    }

    static void removeAlliances(Guild g)
    {
        for (Alliance a : getGuildAlliances(g))
        {
            a.delete();
            alliances.remove(a);
        }
    }

    public static void enable()
    {
        RedisChannel.INSTANCE.ALLIANCES.forEach(s -> {
            final Alliance alliance = GsonUtil.fromJson(s, Alliance.class);
            alliances.add(alliance);
        });
        Logger.info("Loaded " + alliances.size() + " alliances!");
    }
    public static List<Alliance> getAlliances()
    {
        return alliances;
    }

    public static List<String> getInvites()
    {
        return invites;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static List<Alliance> alliances = new ArrayList();
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<String> invites = new ArrayList();
}
