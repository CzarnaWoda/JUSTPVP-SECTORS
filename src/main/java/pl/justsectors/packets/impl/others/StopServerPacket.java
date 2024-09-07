package pl.justsectors.packets.impl.others;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class StopServerPacket extends RedisPacket {

    private String executor;
    private long time;

    public StopServerPacket(){}
    public StopServerPacket(String executor, long time){
        this.executor = executor;
        this.time = time;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
