package pl.justsectors.packets.impl.ranks;


import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class SetPrefixPacket extends RedisPacket {

    private String rankName;
    private String prefix;

    public SetPrefixPacket(String rankName, String prefix){
        this.rankName = rankName;
        this.prefix = prefix;
    }

    public SetPrefixPacket(){}

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
