package pl.blackwater.guilds.data;

import lombok.*;
import org.redisson.api.RFuture;
import pl.blackwater.core.Core;
import pl.blackwater.guilds.managers.*;
import pl.blackwaterapi.*;
import pl.blackwaterapi.store.*;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.redis.channels.RedisChannel;

import java.sql.*;

@Data
public class Alliance implements Entry {

    private Guild guild1,guild2;
    private boolean pvp;

    public Alliance(Guild guild1, Guild guild2){
        this.guild1 = guild1;
        this.guild2 = guild2;
        this.pvp = false;
    }

    @Override
    public void insert() {
        RedisChannel.INSTANCE.ALLIANCES.addAsync(GsonUtil.toJson(this));
    }

    @Override
    public void update(boolean b) {
        throw new RuntimeException("Can not update this object!");
    }

    @Override
    public void delete() {
        RedisChannel.INSTANCE.ALLIANCES.remove(GsonUtil.toJson(this));
    }
}
