package pl.justsectors.packets.impl.user;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

@Getter
public class RemoveStatisticPacket extends RedisPacket {

    private UUID userUUID;
    private String statistic;
    private float amount;


    public RemoveStatisticPacket(){}
    public RemoveStatisticPacket(UUID userUUID, String statistic, float amount){
        this.userUUID = userUUID;
        this.statistic = statistic;
        this.amount = amount;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
