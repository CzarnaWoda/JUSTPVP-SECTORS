package pl.justsectors.packets.impl.customevents;

import lombok.Data;
import lombok.NonNull;
import pl.blackwater.core.events.EventType;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;


@Data
public class StartCustomEventPacket extends RedisPacket {

    @NonNull private EventType eventType;
    @NonNull private long eventTime;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
