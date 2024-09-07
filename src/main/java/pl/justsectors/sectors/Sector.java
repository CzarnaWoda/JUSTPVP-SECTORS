package pl.justsectors.sectors;

import org.redisson.api.RSet;
import pl.justsectors.redis.RedisManager;

import java.util.HashSet;
import java.util.Set;

public class Sector {

    private final String sectorName;
    private final Set<String> onlinePlayers;
    private final RSet<String> redisOnlinePlayers;
    private double tps;
    private Long lastUpdate;
    private SectorType sectorType;

    public Sector(String sectorName, SectorType type) {
        this.sectorName = sectorName;
        this.onlinePlayers = new HashSet<>();
        this.redisOnlinePlayers = RedisManager.getRedisConnection().getSet(sectorName + "-online");
        this.tps = 20;
        this.lastUpdate = -1L;
        this.sectorType = type;
    }

    public SectorType getSectorType() {
        return sectorType;
    }

    public String getSectorName() {
        return sectorName;
    }

    public Set<String> getOnlinePlayers() {
        //REDIS NEW SYSTEM <- SEARCH
        //return onlinePlayers;
        return this.redisOnlinePlayers;
    }

    public double getTps() {
        return tps;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setTps(double tps) {
        this.tps = tps;
    }

    public RSet<String> getRedisOnlinePlayers() {
        return redisOnlinePlayers;
    }

    public boolean isOnline()
    {
        return System.currentTimeMillis() - this.lastUpdate < 8000L;
    }
}
