package pl.justsectors.packets.impl.teleport;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class PlayerToPlayerTeleportPacket extends RedisPacket {

    private UUID playerTeleport;
    private UUID targetPlayer;

    public PlayerToPlayerTeleportPacket() {
    }

    public PlayerToPlayerTeleportPacket(UUID playerTeleport, UUID targetPlayer) {
        this.playerTeleport = playerTeleport;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getPlayerTeleport() {
        return playerTeleport;
    }

    public UUID getTargetPlayer() {
        return targetPlayer;
    }
}
