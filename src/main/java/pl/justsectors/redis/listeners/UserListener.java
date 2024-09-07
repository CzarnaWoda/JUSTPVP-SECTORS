package pl.justsectors.redis.listeners;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.guilds.ranking.RankingManager;
import pl.justsectors.nbt.NBTManager;
import pl.justsectors.packets.impl.user.UserChangeSectorPacket;
import pl.justsectors.redis.RedisManager;
import pl.justsectors.redis.enums.ChannelType;
import pl.justsectors.redis.listeners.api.RedisListener;

public class UserListener<T extends UserChangeSectorPacket> extends RedisListener<T> {


    public UserListener(ChannelType type, RTopic redisTopic) {
        super(type, redisTopic);
        redisTopic.addListener(UserChangeSectorPacket.class, new MessageListener<UserChangeSectorPacket>() {
            @Override
            public void onMessage(CharSequence charSequence, UserChangeSectorPacket packet) {
                User u = packet.getUser();
                u.setLastUpdate(System.currentTimeMillis());
                final User oldUser = UserManager.getUser(u.getUuid());
                RankingManager.removeRanking(oldUser);
                UserManager.registerUser(u);
                RankingManager.addRanking(u);
                final EntityPlayer entityPlayer = u.asEntityPlayer();
                if (entityPlayer != null) {
                    final NBTTagCompound nbtTagCompound = NBTManager.asNBT(u.getNbtString());
                    u.setLastUpdate(System.currentTimeMillis());
                    User finalU = u;
                    MinecraftServer.getServer().postToMainThread(() -> {
                        final Player p = u.asPlayer();
                        if(p != null && p.isOnline()) {
                            NBTManager.applyToPlayer(finalU, nbtTagCompound);
                        }
                    });
                }
            }
        });
    }


    @Override
    public void sendMessage(UserChangeSectorPacket message) {
        getTopic().publish(message);
    }

    @Override
    public void sendMessage(String sector, T data) {
        RedisManager.getRedisConnection().getTopic(sector + "-user").publish(data);
    }
}
