package pl.justsectors.packets.impl.guild;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;


@Data
public class GuildSetSoulsPacket extends RedisPacket {

    @NonNull
    private String guildTag;
    @NonNull
    private int amonut;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
