package pl.justsectors.packets.impl.treasures;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class TreasureDeletePacket extends RedisPacket {

    private UUID treasureUUID;
    private String treasureType;

    public TreasureDeletePacket() {
    }

    public TreasureDeletePacket(UUID treasureUUID, String treasureType) {
        this.treasureUUID = treasureUUID;
        this.treasureType = treasureType;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getTreasureUUID() {
        return treasureUUID;
    }

    public String getTreasureType() {
        return treasureType;
    }
}
