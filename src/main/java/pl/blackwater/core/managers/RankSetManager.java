package pl.blackwater.core.managers;

import lombok.Getter;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.data.RankSet;
import pl.blackwater.core.data.User;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.redis.channels.RedisChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class RankSetManager {
    @Getter
    private static HashMap<UUID, RankSet> ranksets = new HashMap<>();

    public static void setRank(User user, String admin, Rank rank, long time){
        if(ranksets.get(user.getUuid()) != null){
            removeRank(user, ranksets.get(user.getUuid()));
        }
        RankSet rankSet = new RankSet(user.getUuid(), rank.getName(), user.getRank(), admin, time);
        ranksets.put(user.getUuid(), rankSet);
        user.setRank(rank.getName());
        Core.getRankManager().unimplementPermissions(user);
        Core.getRankManager().implementPermissions(user);
    }
    public static void removeRank(User u, RankSet rankSet){
        u.setRank(rankSet.getPreviousRank());
        Core.getRankManager().unimplementPermissions(u);
        Core.getRankManager().implementPermissions(u);
        ranksets.remove(u.getUuid());
        rankSet.delete();
    }
    public static RankSet getSetRank(User u){
        return ranksets.get(u.getUuid());
    }
    public static void setup(){
        RedisChannel.INSTANCE.RANKS.forEach(((uuid, s) -> {
            final RankSet e = GsonUtil.fromJson(s, RankSet.class);
            ranksets.put(e.getUser(), e);
        }));
        Logger.info("Loaded " + ranksets.size() + " ranksets!");
    }
}
