package pl.justsectors.packets.impl.mines;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class MineBuyPacket extends RedisPacket {

    private int mineID;
    private UUID buyerUUID;

    public MineBuyPacket(int mineID, UUID buyerUUID) {
        this.mineID = mineID;
        this.buyerUUID = buyerUUID;
    }

    public MineBuyPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public int getMineID() {
        return mineID;
    }

    public UUID getBuyerUUID() {
        return buyerUUID;
    }
}
