package pl.blackwater.core.data;

import lombok.Data;
import pl.blackwater.core.Core;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.redis.channels.RedisChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Data
public class RankSet implements Entry {
    private UUID user;
    private String rank,previousRank,admin;
    private Long expireTime;

    public RankSet(UUID user, String rank, String previousRank, String admin, Long expireTime){
        this.user = user;
        this.rank = rank;
        this.previousRank = previousRank;
        this.admin = admin;
        this.expireTime = expireTime;

        insert();
    }


    @Override
    public void insert() {
        RedisChannel.INSTANCE.RANKS.putAsync(this.user, GsonUtil.toJson(this));
    }

    @Override
    public void update(boolean b) {
        Logger.warning("ERROR #1 -> RankSet can't be update ! Check code and make a change !");
    }

    @Override
    public void delete() {
        RedisChannel.INSTANCE.RANKS.removeAsync(this.user);
    }
}
