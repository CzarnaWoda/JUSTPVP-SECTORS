package pl.justsectors.packets.impl.auctions;

import pl.blackwater.market.data.Auction;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class AuctionSoldPacket extends RedisPacket {

    private int index;
    private String buyerName;
    private UUID auctionOwner;

    public AuctionSoldPacket(int index, String buyerName, UUID auctionOwner) {
        this.index = index;
        this.buyerName = buyerName;
        this.auctionOwner = auctionOwner;
    }

    public AuctionSoldPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public int getIndex() {
        return index;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public UUID getAuctionOwner() {
        return auctionOwner;
    }
}
