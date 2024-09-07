package pl.justsectors.packets.impl.ranks;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;


@Getter
public class CreateRankPacket extends RedisPacket {

    private String rankName;

    public CreateRankPacket(){}

    public CreateRankPacket(String rankName){
        this.rankName = rankName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
