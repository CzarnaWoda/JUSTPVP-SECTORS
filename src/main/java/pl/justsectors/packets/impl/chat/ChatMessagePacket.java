package pl.justsectors.packets.impl.chat;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class ChatMessagePacket extends RedisPacket {

    private String message;

    public ChatMessagePacket(String message){
        this.message = message;
    }

    public ChatMessagePacket(){

    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return message;
    }
}
