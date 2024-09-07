package pl.justsectors.packets.impl.itemshop;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class AddSlotReservationPacket extends RedisPacket {

    private String userName;


    @Override
    public void handlePacket(PacketHandler handler) {
    }

    public String getUserName() {
        return userName;
    }
}
