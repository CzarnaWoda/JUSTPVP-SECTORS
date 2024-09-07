package pl.justsectors.packets.impl.customevents;

import lombok.Data;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Data
public class EndCustomEventPacket extends RedisPacket {

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
