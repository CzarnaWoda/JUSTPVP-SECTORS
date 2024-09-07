package pl.justsectors.redis.factory;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import pl.blackwater.core.settings.CoreConfig;

public class RedisFactory {

    public static RedissonClient createRedisConnection()
    {
        org.redisson.config.Config config = new org.redisson.config.Config();
        config.useSingleServer()
                .setRetryAttempts(Integer.MAX_VALUE)
                .setRetryInterval(50)
                .setAddress("redis://" + CoreConfig.REDIS_SERVER + ":6379")
                .setPassword(CoreConfig.REDIS_PASSWORD);
        return Redisson.create(config);
    }

}
