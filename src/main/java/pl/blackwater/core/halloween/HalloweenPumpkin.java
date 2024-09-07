package pl.blackwater.core.halloween;

import org.bukkit.Location;

public class HalloweenPumpkin {

    private final int pumpkinID;
    private final String pumpkinName;
    private final Location location;


    public HalloweenPumpkin(int pumpkinID, String pumpkinName, Location location) {
        this.pumpkinID = pumpkinID;
        this.pumpkinName = pumpkinName;
        this.location = location;
    }


    public int getPumpkinID() {
        return pumpkinID;
    }

    public String getPumpkinName() {
        return pumpkinName;
    }

    public Location getLocation() {
        return location;
    }
}
