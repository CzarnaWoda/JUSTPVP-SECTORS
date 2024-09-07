package pl.justsectors.packets.impl.auctions;

import pl.blackwater.market.data.Auction;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class AuctionRegisterPacket extends RedisPacket {

    private Auction auction;
    private String ownerName;

    public AuctionRegisterPacket(Auction auction, String ownerName) {
        this.auction = auction;
        this.ownerName = ownerName;
    }

    public AuctionRegisterPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public Auction getAuction() {
        return auction;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
