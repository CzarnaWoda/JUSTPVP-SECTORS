package pl.blackwaterapi.teleport;

import pl.blackwater.core.data.User;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.teleport.PlayerToPlayerTeleportPacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.SectorManager;

public class PlayerTeleport extends TeleportRequest {

    private final User targetUser;

    public PlayerTeleport(User teleportUser, User targetUser) {
        super(teleportUser);
        this.targetUser = targetUser;
    }

    @Override
    public void reuqestAccepted() {
        if (this.targetUser.getSectorName().equalsIgnoreCase(CoreConfig.CURRENT_SECTOR_NAME)) {
            this.getTeleportPlayer().teleport(this.targetUser.asPlayer());
            this.getTeleportPlayer().sendMessage(Util.fixColor("&8>> &7Teleportacja przebiegla pomyslnie!"));
            return;
        }
        final RedisPacket teleportPacket = new PlayerToPlayerTeleportPacket(this.getTeleportPlayer().getUniqueId(), this.targetUser.getUuid());
        RedisClient.sendPacket(teleportPacket, this.targetUser.getSector());
        SectorManager.sendToSector(this.getTeleportUser(), this.targetUser.getSector());
    }
}
