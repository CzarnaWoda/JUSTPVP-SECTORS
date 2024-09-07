package pl.justsectors.packets.impl.teleport;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class PlayerSpawnTeleportPacket extends RedisPacket {

    private UUID uuid;

    public PlayerSpawnTeleportPacket() {
    }

    public PlayerSpawnTeleportPacket(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUuid() {
        return uuid;
    }
}
