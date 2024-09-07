package pl.justsectors.packets.impl.config;

import lombok.Data;
import lombok.NonNull;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

@Data
public class SetConfigDoublePacket extends RedisPacket {

    @NonNull
    private String configName;
    @NonNull private double variable;
    @NonNull private String path;
    @NonNull private boolean openAdminPanelInventory;
    @NonNull private String packetExecutor;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
