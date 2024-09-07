package pl.justsectors.packets.impl.config;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;


@Data
public class LoadConfigPacket extends RedisPacket {

    @NonNull private String config;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
