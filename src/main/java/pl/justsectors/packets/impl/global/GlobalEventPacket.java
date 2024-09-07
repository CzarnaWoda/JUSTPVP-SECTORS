package pl.justsectors.packets.impl.global;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class GlobalEventPacket extends RedisPacket {
    private String eventName;
    private long time;
    private String admin;
    private String multipler;

    public GlobalEventPacket(){}
    public GlobalEventPacket(final String eventName, final long time, final String admin, final String multipler){
        this.eventName = eventName;
        this.time=time;
        this.admin =admin;
        this.multipler = multipler;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
