package pl.justsectors.packets.impl.user;

import lombok.Getter;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;
import pl.justsectors.redis.client.RedisClient;

@Getter
public class UserRemoveEventPacket extends RedisPacket {
    private String userName;
    private String eventName;

    public UserRemoveEventPacket(){}

    public UserRemoveEventPacket(String userName, String eventName){
        this.userName = userName;
        this.eventName = eventName;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
