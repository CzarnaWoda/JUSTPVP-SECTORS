package pl.justsectors.packets.impl.teleport;

import org.bukkit.Bukkit;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.teleport.TeleportRequest;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;

public class SpawnTeleport extends TeleportRequest {

    private final Sector spawnSector;

    public SpawnTeleport(User teleportUser, Sector spawnSector) {
        super(teleportUser);
        this.spawnSector = spawnSector;
    }

    @Override
    public void reuqestAccepted() {
        final Sector sector = this.getTeleportUser().getSector();
        if(sector == null)
        {
            this.getTeleportPlayer().sendMessage(Util.fixColor("&8>> &7Teleportacja nie powiodla sie... sektor wylaczony!"));
            return;
        }
        final User user = UserManager.getUser(getTeleportUser().getUuid());
        if (this.getTeleportUser().getSector().getSectorName().equalsIgnoreCase(spawnSector.getSectorName())) {
            this.getTeleportPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
            this.getTeleportPlayer().sendMessage(Util.fixColor("&8>> &7Teleportacja przebiegla pomyslnie!"));
            return;
        }
        final RedisPacket packet = new PlayerSpawnTeleportPacket(getTeleportPlayer().getUniqueId());
        RedisClient.sendPacket(packet, spawnSector.getSectorName());
        SectorManager.sendToSector(getTeleportUser(), spawnSector);
    }
}
