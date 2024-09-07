package pl.justsectors.packets.impl.chat;

import lombok.Getter;
import pl.blackwater.core.enums.ChatControlType;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class ChatControlGlobalMessagePacket extends RedisPacket {

    private String message;
    private ChatControlType type;

    public ChatControlGlobalMessagePacket(){}

    public ChatControlGlobalMessagePacket(String message, ChatControlType type){
        this.message = message;
        this.type = type;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
