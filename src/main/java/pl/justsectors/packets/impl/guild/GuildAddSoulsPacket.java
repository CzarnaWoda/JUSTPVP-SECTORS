package pl.justsectors.packets.impl.guild;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Data
public class GuildAddSoulsPacket extends RedisPacket {

    @NonNull
    private String guildTag;
    @NonNull
    private int amount;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
