package pl.justsectors.packets.impl.ranks;


import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Getter
public class RemovePermissionPacket extends RedisPacket {

    private String rankName;
    private String permission;

    public RemovePermissionPacket(){}

    public RemovePermissionPacket(String rankName, String permission){
        this.rankName = rankName;
        this.permission = permission;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
