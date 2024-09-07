package pl.blackwater.core.tasks;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.tasks.api.ScheduledTask;
import pl.justsectors.packets.impl.sectors.SectorStatusPacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SectorStatusTask extends ScheduledTask {

    public SectorStatusTask(ScheduledExecutorService executorService) {
        super(executorService);
    }

    @Override
    public void runTask() {
        getExecutorService().scheduleAtFixedRate(() -> {
            RedisClient.sendGlobalPacket(new SectorStatusPacket(CoreConfig.CURRENT_SECTOR_NAME, MinecraftServer.getServer().recentTps[0], Bukkit.getOnlinePlayers().size()));
        }, 1, 3, TimeUnit.SECONDS);
    }
}
