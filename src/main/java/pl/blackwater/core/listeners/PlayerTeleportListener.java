package pl.blackwater.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;

public class PlayerTeleportListener implements Listener{
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onThrowPearl(PlayerTeleportEvent e) {
		if(e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
		final User u = UserManager.getUser(e.getPlayer());
		u.addThrowPearlViaPacket(1);
	}
}
