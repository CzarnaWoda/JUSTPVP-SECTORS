package pl.justsectors.packets.impl.chat;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class TellMessagePacket extends RedisPacket {

    private UUID senderUUID;
    private String senderName;
    private UUID receiverUUID;
    private String receiverName;
    private String message;

    public TellMessagePacket(UUID senderUUID, String senderName, UUID receiverUUID, String receiverName, String message) {
        this.senderUUID = senderUUID;
        this.senderName = senderName;
        this.receiverUUID = receiverUUID;
        this.receiverName = receiverName;
        this.message = message;
    }


    public TellMessagePacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getSenderUUID() {
        return senderUUID;
    }

    public String getSenderName() {
        return senderName;
    }

    public UUID getReceiverUUID() {
        return receiverUUID;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessage() {
        return message;
    }
}
