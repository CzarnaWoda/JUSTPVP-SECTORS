package pl.justsectors.packets.impl.chat;

import lombok.Getter;
import pl.blackwater.core.enums.ChatControlType;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

@Getter
public class ChatControlMessagePacket extends RedisPacket {

    private UUID userUUID;
    private ChatControlType chatControlType;
    private String message;

    public ChatControlMessagePacket(){}

    public ChatControlMessagePacket(UUID userUUID, ChatControlType type, String message){
        this.userUUID = userUUID;
        this.chatControlType = type;
        this.message = message;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
