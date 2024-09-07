package pl.justsectors.packets.impl.user;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class UserEatRefPacket extends RedisPacket {

    private UUID userUUID;

    public UserEatRefPacket(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public UserEatRefPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUserUUID() {
        return userUUID;
    }
}
