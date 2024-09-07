package pl.justsectors.packets.impl.chat;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Data
public class ChatTogglePacket extends RedisPacket {
    @NonNull private String sener;
    @NonNull private boolean status;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
