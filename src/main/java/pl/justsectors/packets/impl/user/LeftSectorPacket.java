package pl.justsectors.packets.impl.user;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class LeftSectorPacket extends RedisPacket {

    private String sectorName;
    private String userName;

    public LeftSectorPacket(){}
    public LeftSectorPacket(String userName, String sectorName){
        this.userName = userName;
        this.sectorName = sectorName;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
