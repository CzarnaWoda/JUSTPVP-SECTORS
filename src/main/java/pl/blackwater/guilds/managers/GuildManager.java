package pl.blackwater.guilds.managers;

import lombok.*;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.*;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.ranking.*;
import pl.blackwater.guilds.scoreboard.*;
import pl.blackwaterapi.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.redis.channels.RedisChannel;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

public class GuildManager {
    @Getter private static final Map<String, Guild> guilds = new HashMap<>();


    public static Guild createLocalGuild(String tag, String name, Player owner){
        return new Guild(tag, name, owner);
    }

    public static Guild createGuild(String tag, String name, Player owner){
        return createGuild(tag, name, owner.getUniqueId());
    }

    public static void registerGuild(final Guild g)
    {
        guilds.putIfAbsent(g.getTag().toUpperCase(), g);
    }

    public static Guild createGuild(String tag, String name, UUID owner){
        Guild g = new Guild(tag, name, owner);
        guilds.put(g.getTag().toUpperCase(),g);
        RankingManager.addRanking(g);
        return g;
    }

    public static void removeGuild(Guild g){
        RankingManager.removeRanking(g);
        ScoreBoardNameTag.updateBoard(g);
        AllianceManager.removeAlliances(g);
        guilds.remove(g.getTag().toUpperCase());
    }

    public static Guild getGuild(String tag){
        return guilds.get(tag.toUpperCase());
    }

    public static Guild getGuild(Player p){
        for(Guild g : guilds.values()){
            if(g.isMember(p.getUniqueId())){
                return g;
            }
        }
        return null;
    }

    public static void setup(){
        MinecraftServer.getServer().postToMainThread(() -> {
            RedisChannel.INSTANCE.GUILDS.forEach(((tag, s) -> {
                final Guild guild = GsonUtil.fromJson(s, Guild.class);
                guilds.put(guild.getTag().toUpperCase(), guild);
                guild.setup();
                RankingManager.addRanking(guild);
            }));
        });
        Logger.info("Loaded " + getGuilds().size() + " guilds!");
    }
    public static void resetup(){
        getGuilds().clear();
        setup();
    }
}
