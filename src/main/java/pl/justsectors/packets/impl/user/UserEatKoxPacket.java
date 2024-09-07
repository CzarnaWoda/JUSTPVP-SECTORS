package pl.justsectors.packets.impl.user;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class UserEatKoxPacket extends RedisPacket {

    private UUID userUUID;

    public UserEatKoxPacket(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public UserEatKoxPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUserUUID() {
        return userUUID;
    }
}
