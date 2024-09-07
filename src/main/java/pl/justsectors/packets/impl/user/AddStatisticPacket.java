package pl.justsectors.packets.impl.user;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

@Getter
public class AddStatisticPacket extends RedisPacket {

    private UUID userUUID;
    private String stat;
    private float amount;

    public AddStatisticPacket(){}

    public AddStatisticPacket(UUID userUUID, String stat, float amount){
        this.userUUID = userUUID;
        this.stat= stat;
        this.amount =amount;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

}
