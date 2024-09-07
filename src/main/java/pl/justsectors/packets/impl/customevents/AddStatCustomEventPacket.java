package pl.justsectors.packets.impl.customevents;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;


@Data
public class AddStatCustomEventPacket extends RedisPacket {

    @NonNull private UUID uuid;
    @NonNull private int amount;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
