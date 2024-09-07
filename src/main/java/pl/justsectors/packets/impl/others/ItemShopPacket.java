package pl.justsectors.packets.impl.others;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;


@Data
public class ItemShopPacket extends RedisPacket {
    @NonNull private String playerName,name;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
