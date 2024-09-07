package pl.justsectors.packets.impl.user;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class UserThrowPearl extends RedisPacket {

    private UUID userUUID;

    public UserThrowPearl(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public UserThrowPearl() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUserUUID() {
        return userUUID;
    }
}
