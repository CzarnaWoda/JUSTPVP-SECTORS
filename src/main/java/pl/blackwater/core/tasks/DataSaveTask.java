package pl.blackwater.core.tasks;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.tasks.api.ScheduledTask;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.nbt.NBTManager;
import pl.justsectors.packets.impl.user.LeftSectorPacket;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.redis.client.RedisClient;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataSaveTask extends ScheduledTask {

    public DataSaveTask(ScheduledExecutorService executorService) {
        super(executorService);
    }

    @Override
    public void runTask() {
        getExecutorService().scheduleAtFixedRate(() -> {
            for(Player p : Bukkit.getOnlinePlayers()) {
                final User u = UserManager.getUser(p.getUniqueId());
                if (u != null)
                {
                    if(System.currentTimeMillis() - u.getLastUpdate() >= TimeUnit.SECONDS.toMillis(60)) {
                        u.setFly(p.getAllowFlight());
                        u.setGameMode(p.getGameMode());
                        u.setLastName(p.getName());
                        u.setLastLocation(p.getLocation());
                        u.addTimePlay((int) ((System.currentTimeMillis() - u.getLastJoin()) / 1000L));
                        u.setGod(p.hasPermission("core.god"));
                        final NBTTagCompound nbtTagCompound = NBTManager.createNBTBasedOnUser(u);
                        if (nbtTagCompound == null) {
                            p.kickPlayer("Blad podczas zapisu Twojego ekwipunku!");
                            return;
                        }
                        u.setNbtString(nbtTagCompound.toString());
                        u.setLastUpdate(System.currentTimeMillis());
                        RedisChannel.INSTANCE.USERS.put(u.getUuid(), GsonUtil.toJson(u));
                    }
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }
}
