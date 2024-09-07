package pl.justsectors.packets.impl.others;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class SectorDisablePacket extends RedisPacket {

    private String sectorName;

    public SectorDisablePacket(String sectorName) {
        this.sectorName = sectorName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getSectorName() {
        return sectorName;
    }
}
