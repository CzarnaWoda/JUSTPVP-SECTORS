package pl.justsectors.packets.impl.ranks;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class SetSuffixPacket extends RedisPacket {

    private String rankName;
    private String suffix;

    public SetSuffixPacket(String rankName, String suffix){
        this.rankName = rankName;
        this.suffix = suffix;
    }

    public SetSuffixPacket(){}

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
