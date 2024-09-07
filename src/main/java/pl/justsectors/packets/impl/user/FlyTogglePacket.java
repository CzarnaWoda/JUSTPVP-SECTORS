package pl.justsectors.packets.impl.user;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class FlyTogglePacket extends RedisPacket {

    private String userName;
    private String executor;


    public FlyTogglePacket(){}
    public FlyTogglePacket(String userName, String executor){
        this.userName = userName;
        this.executor = executor;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);

    }
}
