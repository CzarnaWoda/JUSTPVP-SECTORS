package pl.blackwater.core.halloween;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.Pumpkin;
import pl.blackwater.core.Core;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blazingpack.waypoints.WaypointBuilder;
import pl.blazingpack.waypoints.WaypointManager;
import pl.justsectors.redis.channels.RedisChannel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class HalloweenManager {

    private final static List<Location> locations = new ArrayList<>();
    private final static Set<HalloweenPumpkin> activePumpkins = new HashSet<>();
    private static final int MAX_PUMPKINS = 3;
    private static final AtomicInteger ai = new AtomicInteger(0);
    private static final Cache<Location, Long> lastPumpkins = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

    public static HalloweenPumpkin createAndRespPumpkin() {
        if(activePumpkins.size() >= MAX_PUMPKINS ) {
            return null;
        }

        Location location = getUniqueLocation();
        if (location == null) {
            return null;
        }
        location.getBlock().setType(Material.PUMPKIN);
        final HalloweenPumpkin pumpkin = new HalloweenPumpkin(ai.getAndIncrement(), "Halloween", location);
        activePumpkins.add(pumpkin);
        for(Player p : Bukkit.getOnlinePlayers()) {
            WaypointBuilder.builder().withAction((byte) 0)
                    .withBlockPosition(System.currentTimeMillis())
                    .withRgb(0xFF9748)
                    .withLocation(location.clone().add(0, 1, 0))
                    .withTime(TimeUnit.DAYS.toMillis(1))
                    .withWaypointID(pumpkin.getPumpkinID())
                    .withWaypointName(pumpkin.getPumpkinName())
                    .withType(4)
                    .withImage_hash("c4957e31e53f2112b3e3812046edb60d92af6e4e")
                    .withImage_size(54704).build().sendPlayer(p);
        }
        return pumpkin;
    }

    private static Location getUniqueLocation() { //dla wiekszej ilosci respu np 30 zabezpieczyc przez nieskonczona petla, dla 3 jebac
        final Location location = locations.get(ThreadLocalRandom.current().nextInt(locations.size() - 1));
        if(lastPumpkins.getIfPresent(location) != null) {
            return getUniqueLocation();
        }
        for (HalloweenPumpkin activePumpkin : activePumpkins) {
            if(activePumpkin.getLocation() == location || location.getBlock().getType() != null && location.getBlock().getType().equals(Material.PUMPKIN)) {
                return getUniqueLocation();
            }
        }
        return location;
    }

    public static HalloweenPumpkin createPumpkin(final Location location) {
        return new HalloweenPumpkin(ai.getAndIncrement(), "Halloween", location);
    }

    private static void createDeleteWaypoint(final HalloweenPumpkin pumpkin) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            WaypointManager.deleteWaypoint(p, pumpkin.getPumpkinID());
            WaypointBuilder.builder().withAction((byte) 0)
                    .withBlockPosition(System.currentTimeMillis())
                    .withRgb(0x12F729)
                    .withLocation(pumpkin.getLocation().clone().add(0, 1, 0))
                    .withTime(TimeUnit.SECONDS.toMillis(10))
                    .withWaypointID(pumpkin.getPumpkinID())
                    .withWaypointName("Dynia zostala zebrana!")
                    .withType(4)
                    .withImage_hash("80b80033421018fa314ef50c176331b71b3655ef")
                    .withImage_size(9120).build().sendPlayer(p);
        }
    }

    public static void deletePumpkin(final int pumpkinID) {
        for (HalloweenPumpkin pumpkin : activePumpkins) {
            if(pumpkin.getPumpkinID() == pumpkinID) {
                pumpkin.getLocation().getBlock().setType(Material.AIR);
                activePumpkins.remove(pumpkin);
                createDeleteWaypoint(pumpkin);
                return;
            }
        }
    }

    public static boolean deletePumpkin(final Location location) {
        for (HalloweenPumpkin pumpkin : activePumpkins) {
            if(pumpkin.getLocation().distance(location) <= 2) {
                location.getBlock().setType(Material.AIR);
                activePumpkins.remove(pumpkin);
                createDeleteWaypoint(pumpkin);
                lastPumpkins.put(location, System.currentTimeMillis());
                return true;
            }
        }
        return false;
    }

    public static void clearAllSpots() {
        for (Location location : locations) {
            location.getBlock().setType(Material.AIR);
        }
        activePumpkins.clear();
    }

    public static void registerLocationToResp(final Location location) {
        if(!locations.contains(location)) {
            locations.add(location);
        }
    }

    public static void setup() {
        clearAllSpots();
        for (String location : RedisChannel.INSTANCE.HALLOWEEN_LOCATIONS) {
            final Location loc = GsonUtil.fromJson(location, Location.class);
            registerLocationToResp(loc);
        }
        Core.getPlugin().getLogger().log(Level.INFO, "Zarejestrowano " + locations.size() + " miejsc do respienia dyn!");
    }


    public static Set<HalloweenPumpkin> getActivePumpkins() {
        return activePumpkins;
    }
}
