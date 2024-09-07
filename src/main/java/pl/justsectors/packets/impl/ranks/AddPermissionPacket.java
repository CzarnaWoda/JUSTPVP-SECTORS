package pl.justsectors.packets.impl.ranks;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class AddPermissionPacket extends RedisPacket {

    private String rankName;
    private String permission;

    public AddPermissionPacket(){}
    public AddPermissionPacket(String rankName, String permission){
        this.rankName = rankName;
        this.permission = permission;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
