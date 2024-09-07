package pl.justsectors.packets.impl.user;

import lombok.Getter;
import pl.blackwater.core.utils.StringUtil;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

@Getter
public class UserKickPacket extends RedisPacket {

    private UUID targetUUID;
    private String reason;

    public UserKickPacket(UUID targetUUID, String reason){
        this.targetUUID = targetUUID;this.reason = reason;
    }
    public UserKickPacket(){}

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
