package pl.justsectors.packets.impl.sectors;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class SectorStatusPacket extends RedisPacket {

    private String sectorName;
    private double sectorTPS;
    private int onlinePlayers;

    public SectorStatusPacket(String sectorName, double sectorTPS, int onlinePlayers) {
        this.sectorName = sectorName;
        this.sectorTPS = sectorTPS;
        this.onlinePlayers = onlinePlayers;
    }

    public SectorStatusPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }


    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public String getSectorName() {
        return sectorName;
    }

    public double getSectorTPS() {
        return sectorTPS;
    }
}
