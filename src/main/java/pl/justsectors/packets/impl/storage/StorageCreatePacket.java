package pl.justsectors.packets.impl.storage;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Data
public class StorageCreatePacket extends RedisPacket {
    @NonNull private String signLocation,storageRegion, interactBlockLocation;
    @NonNull private long rentTime;
    @NonNull private  int cost;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
