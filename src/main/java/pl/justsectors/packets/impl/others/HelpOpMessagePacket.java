package pl.justsectors.packets.impl.others;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class HelpOpMessagePacket extends RedisPacket {

    private String message;
    private String executor;


    public HelpOpMessagePacket(){}
    public HelpOpMessagePacket(String message,String executor){
        this.message = message;
        this.executor = executor;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
