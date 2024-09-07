package pl.justsectors.packets.impl.warps;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class WarpDeletePacket extends RedisPacket {

    private String warpName;
    private UUID executorUUID;

    public WarpDeletePacket(String warpName, UUID executorUUID) {
        this.warpName = warpName;
        this.executorUUID = executorUUID;
    }

    public WarpDeletePacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getWarpName() {
        return warpName;
    }

    public UUID getExecutorUUID() {
        return executorUUID;
    }
}
