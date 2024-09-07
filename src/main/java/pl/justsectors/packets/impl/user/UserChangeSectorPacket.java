package pl.justsectors.packets.impl.user;

import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;
import pl.blackwater.core.data.User;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.io.Serializable;
import java.util.UUID;

public class UserChangeSectorPacket extends RedisPacket implements Serializable {

    private String userString;


    public UserChangeSectorPacket(final User user) {
        this.userString = GsonUtil.toJson(user);
    }

    public UserChangeSectorPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
       /* final User user = getUser();
        Bukkit.broadcastMessage("UserChangeSectorPacket dla " + user.getLastName());*/
        //Handled in UserListener
    }

    public User getUser() {
        return GsonUtil.fromJson(this.userString, User.class);
    }
}
