package pl.justsectors.packets.impl.global;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class GlobalGiftKeysPacket extends RedisPacket {
    private String executor;
    private String chestName;
    private int amount;

    public GlobalGiftKeysPacket(){}

    public GlobalGiftKeysPacket(String executor,String chestName, int amount){
        this.executor = executor;
        this.chestName = chestName;
        this.amount = amount;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
