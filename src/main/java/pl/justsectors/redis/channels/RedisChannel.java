package pl.justsectors.redis.channels;

import org.redisson.api.*;
import pl.blackwater.core.Core;
import pl.blackwater.core.settings.CoreConfig;
import pl.justsectors.redis.RedisManager;

import java.util.UUID;
import java.util.logging.Level;

public class RedisChannel {

    public static RedisChannel INSTANCE = new RedisChannel();
    public RTopic globalPacketTopic;
    public RTopic currentSectorTopic;
    public RTopic userTopic;
    public RTopic globalSpigotPacketTopic;
    public RTopic proxiesPacketTopic;
    public RMap<String, String> GUILDS;
    public RMap<UUID, String> USERS;
    public RMap<UUID, String> BACKUPS;
    public RMap<Integer, String> MINES;
    public RMap<String, String> WARPS;
    public RMap<UUID, String> TREASURE_CHESTS;
    public RMap<Integer, String> STORAGE;
    public RMap<Integer, String> AUCTIONS;
    public RMap<UUID, String> RANKS;
    public RList<String> ALLIANCES;
    public RList<String> WARS;
    public RMap<UUID,String> BANS;
    public RSet<String> HALLOWEEN_LOCATIONS;
    public RBitSet HALLOWEEN_BOOLEAN;

    public void setupChannels()
    {
        globalPacketTopic = RedisManager.getRedisConnection().getTopic("globalPacketTopic");
        globalSpigotPacketTopic = RedisManager.getRedisConnection().getTopic("spigotGlobalPacketTopic");
        proxiesPacketTopic = RedisManager.getRedisConnection().getTopic("proxiesPacketTopic");
        currentSectorTopic = RedisManager.getRedisConnection().getTopic(CoreConfig.CURRENT_SECTOR_NAME);
        userTopic = RedisManager.getRedisConnection().getTopic(CoreConfig.CURRENT_SECTOR_NAME + "-user");
        GUILDS = RedisManager.getRedisConnection().getMap("GUILDS");
        USERS = RedisManager.getRedisConnection().getMap("USERS");
        BACKUPS = RedisManager.getRedisConnection().getMap("BACKUPS");
        MINES = RedisManager.getRedisConnection().getMap("MINES");
        WARPS = RedisManager.getRedisConnection().getMap("WARPS");
        TREASURE_CHESTS = RedisManager.getRedisConnection().getMap("CHESTS");
        STORAGE = RedisManager.getRedisConnection().getMap("STORAGE");
        AUCTIONS = RedisManager.getRedisConnection().getMap("AUCTIONS");
        RANKS = RedisManager.getRedisConnection().getMap("RANKS");
        ALLIANCES = RedisManager.getRedisConnection().getList("ALLIANCES");
        WARS = RedisManager.getRedisConnection().getList("WARS");
        BANS = RedisManager.getRedisConnection().getMap("BANS");
        HALLOWEEN_LOCATIONS = RedisManager.getRedisConnection().getSet("HALLOWEEN_LOCATIONS");
        HALLOWEEN_BOOLEAN = RedisManager.getRedisConnection().getBitSet("HALLOWEEN_BOOLEAN");
    }

    private RedisChannel() { }

}
