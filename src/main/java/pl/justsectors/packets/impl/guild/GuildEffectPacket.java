package pl.justsectors.packets.impl.guild;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.potion.PotionEffectType;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Data
public class GuildEffectPacket extends RedisPacket {

    @NonNull private String type;
    @NonNull private int duration;
    @NonNull private int amplifier;
    @NonNull private String guildTag;


    @Override
    public void handlePacket(PacketHandler handler) {

        handler.handle(this);
    }
}
