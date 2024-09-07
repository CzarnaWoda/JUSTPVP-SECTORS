package pl.blackwaterapi.teleport;

import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;

public abstract class TeleportRequest {

    private final User teleportUser;
    private final Player teleportPlayer;

    public TeleportRequest(User teleportUser) {
        this.teleportUser = teleportUser;
        this.teleportPlayer = this.teleportUser.asPlayer();
    }

    public abstract void reuqestAccepted();

    public User getTeleportUser() {
        return teleportUser;
    }

    public Player getTeleportPlayer() {
        return this.teleportPlayer;
    }
}
