package pl.blackwaterapi.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.blackwater.core.data.User;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.teleport.PlayerTeleportPacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;

public class LocationTeleport extends TeleportRequest {

    private final Sector sector;
    private final Location teleportLocation;

    public LocationTeleport(User teleportUser, Sector sector, Location teleportLocation) {
        super(teleportUser);
        this.sector = sector;
        this.teleportLocation = teleportLocation;
    }

    @Override
    public void reuqestAccepted() {
        if (this.sector.getSectorName().equalsIgnoreCase(CoreConfig.CURRENT_SECTOR_NAME)) {
            this.getTeleportPlayer().teleport(this.teleportLocation);
            this.getTeleportPlayer().sendMessage(Util.fixColor("&8>> &7Teleportacja przebiegla pomyslnie!"));
            return;
        }
        final PlayerTeleportPacket teleportPacket = new PlayerTeleportPacket(this.getTeleportUser().getUuid(), this.teleportLocation.getBlockX(), this.teleportLocation.getBlockY(), this.teleportLocation.getBlockZ());
        RedisClient.sendPacket(teleportPacket, this.sector.getSectorName());
        SectorManager.sendToSector(this.getTeleportUser(), this.sector);
    }
}