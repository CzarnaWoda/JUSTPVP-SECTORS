package pl.blackwater.core.halloween;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.tasks.api.ScheduledTask;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.sectors.SectorStatusPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;
import pl.supereasy.bpaddons.bossbar.BarColor;
import pl.supereasy.bpaddons.bossbar.BarStyle;
import pl.supereasy.bpaddons.bossbar.BlazingBossBar;
import pl.supereasy.bpaddons.bossbar.BossBarBuilder;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PumpkinTask extends ScheduledTask {

    private final UUID notificationUUID = UUID.randomUUID();

    public PumpkinTask(ScheduledExecutorService executorService) {
        super(executorService);
    }

    @Override
    public void runTask() {
        getExecutorService().scheduleAtFixedRate(() -> {
            if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.MINE)
             || SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)){
                return;
            }
            if(!RedisChannel.INSTANCE.HALLOWEEN_BOOLEAN.get(0)) {
                return;
            }
            final HalloweenPumpkin pumpkin = HalloweenManager.createAndRespPumpkin();
            if(pumpkin != null) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    new BlazingBossBar(BossBarBuilder.add(notificationUUID)
                            .style(BarStyle.SOLID)
                            .color(BarColor.BLUE)
                            .title(TextComponent.fromLegacyText(Util.fixColor(Util.replaceString(" &8->> ( &6HALLOWEEN &8)&8<<- &7Nowa &edynia &8( &cX:&4" + Math.round(pumpkin.getLocation().getBlockX()) + " &cY:&4" + Math.round(pumpkin.getLocation().getBlockY()) + " &cZ: &4" + Math.round(pumpkin.getLocation().getZ()) + " &8)" ))))
                            .buildPacket()
                    ).sendNotification(p, 5);
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
}
