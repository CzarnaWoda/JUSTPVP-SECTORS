package pl.blackwaterapi.teleport;

import org.bukkit.Location;
import pl.blackwater.core.interfaces.Teleportable;

public class TeleportableImpl implements Teleportable {

    private final String locationName;
    private final Location teleportLocation;


    public TeleportableImpl(String locationName, Location teleportLocation) {
        this.locationName = locationName;
        this.teleportLocation = teleportLocation;
    }

    @Override
    public Location getLocation() {
        return this.teleportLocation;
    }

}
