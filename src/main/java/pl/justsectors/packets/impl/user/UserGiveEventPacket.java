package pl.justsectors.packets.impl.user;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class UserGiveEventPacket extends RedisPacket {

    private String userName;
    private long time;
    private String eventName;

    public UserGiveEventPacket(){}
    public UserGiveEventPacket(String userName, long time, String eventName){
        this.userName = userName;
        this.time = time;
        this.eventName = eventName;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public long getTime() {
        return time;
    }
}
