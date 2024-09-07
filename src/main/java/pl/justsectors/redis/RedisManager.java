package pl.justsectors.redis;

import org.redisson.api.RedissonClient;
import pl.blackwater.core.Core;
import pl.justsectors.redis.enums.ChannelType;
import pl.justsectors.redis.factory.RedisFactory;
import pl.justsectors.redis.listeners.api.RedisListener;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class RedisManager {

    private final static Map<ChannelType, RedisListener> topics = new HashMap<>();
    private static RedissonClient redisConnection;

    public static void setup()
    {
        redisConnection = RedisFactory.createRedisConnection();
    }

    public static RedisListener getRedisListener(final ChannelType type)
    {
        return topics.get(type);
    }

    public static void registerListener(final RedisListener listener)
    {
        topics.put(listener.getType(), listener);
        Core.getPlugin().getLogger().log(Level.INFO, "Registered redis listener for " + listener.getType().name());
    }


    public static RedissonClient getRedisConnection() {
        return redisConnection;
    }
}
