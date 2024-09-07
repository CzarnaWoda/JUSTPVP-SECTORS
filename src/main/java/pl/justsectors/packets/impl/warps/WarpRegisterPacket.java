package pl.justsectors.packets.impl.warps;

import pl.blackwater.core.data.Warp;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class WarpRegisterPacket extends RedisPacket {

    private UUID executorUUID;
    private Warp warp;

    public WarpRegisterPacket(UUID executorUUID, Warp warp) {
        this.executorUUID = executorUUID;
        this.warp = warp;
    }

    public WarpRegisterPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getExecutorUUID() {
        return executorUUID;
    }

    public Warp getWarp() {
        return warp;
    }
}
