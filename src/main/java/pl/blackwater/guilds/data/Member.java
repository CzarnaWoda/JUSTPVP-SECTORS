package pl.blackwater.guilds.data;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.guilds.managers.*;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.redis.channels.RedisChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


//INFO OWNER,PREOWNER,MOD,NORMAL
@Data
public class Member{
    private UUID uuid;
    private String name;
    private String tag;
    private String position;
    public Member(UUID uuid,String name,String tag,String position){
        this.uuid = uuid;
        this.name = name;
        this.tag = tag;
        this.position = position;
    }

    public Guild getGuild(){
        return GuildManager.getGuild(getTag());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
