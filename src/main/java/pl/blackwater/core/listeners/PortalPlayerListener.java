package pl.blackwater.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.managers.WarpManager;
import pl.blackwaterapi.utils.Util;

import java.util.HashMap;

public class PortalPlayerListener implements Listener {

    private HashMap<Player,Long> times = new HashMap<>();
    @EventHandler
    public void onPortal(PlayerPortalEvent e){
        if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)){
            final Player player = e.getPlayer();
            if(times.get(player) != null && System.currentTimeMillis() - times.get(player) < 4000L) {
                return;
            }
            WarpManager.getRandomWarpInstant(player);
            UserManager.getUser(player.getUniqueId()).setProtection(Util.parseDateDiff("3s",true));
            times.put(player, System.currentTimeMillis());
        }
        if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)){
            final Player player = e.getPlayer();
            if(times.get(player) != null && System.currentTimeMillis() - times.get(player) < 4000L) {
                return;
            }
            WarpManager.getRandomWarpInstant(player);
            times.put(player, System.currentTimeMillis());
        }
    }
}
