package pl.justsectors.packets.impl.chat;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class AdminChatPacket extends RedisPacket {

    private String message;
    public AdminChatPacket(){

    }
    public AdminChatPacket(String message){
        this.message = message;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return message;
    }
}
