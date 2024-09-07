package pl.justsectors.packets.impl.treasures;

import pl.blackwater.core.data.TreasureChest;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class TreasureCreatePacket extends RedisPacket {

    private UUID creatorUUID;
    private TreasureChest treasureChest;

    public TreasureCreatePacket() {
    }

    public TreasureCreatePacket(UUID creatorUUID, TreasureChest treasureChest) {
        this.creatorUUID = creatorUUID;
        this.treasureChest = treasureChest;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getCreatorUUID() {
        return creatorUUID;
    }

    public TreasureChest getTreasureChest() {
        return treasureChest;
    }
}
