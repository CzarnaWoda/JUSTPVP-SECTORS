package pl.justsectors.packets.impl.user;

import lombok.Getter;
import pl.blackwater.core.data.User;
import pl.blackwater.core.utils.StringUtil;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

@Getter
public class SetStatisticPacket extends RedisPacket {

    private UUID userUUID;
    private float amount;
    private String statisticType;

    public SetStatisticPacket(){}

    public SetStatisticPacket(UUID userUUID, float amount, String statisticType){
        this.userUUID = userUUID;
        this.amount = amount;
        this.statisticType = statisticType;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
