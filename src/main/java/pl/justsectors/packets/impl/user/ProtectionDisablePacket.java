package pl.justsectors.packets.impl.user;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;
import pl.justsectors.redis.client.RedisClient;

import java.util.UUID;

@Data
public class ProtectionDisablePacket extends RedisPacket {
    @NonNull private UUID uuid;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
