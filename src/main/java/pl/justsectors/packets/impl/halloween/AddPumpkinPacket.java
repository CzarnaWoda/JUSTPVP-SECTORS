package pl.justsectors.packets.impl.halloween;

import org.bukkit.Location;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class AddPumpkinPacket extends RedisPacket {

    private final Location location;

    public AddPumpkinPacket(Location location) {
        this.location = location;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public Location getLocation() {
        return location;
    }
}
