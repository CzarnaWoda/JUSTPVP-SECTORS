package pl.blackwater.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pl.blackwater.core.commands.VanishCommand;
import pl.blackwater.core.managers.TeleportManager;
import pl.blackwaterapi.utils.Util;

public class VanishListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		for(Player vanished : VanishCommand.getVanished()) {
			final Player p = e.getPlayer();
			if(!p.hasPermission("core.vanish"))
				p.hidePlayer(vanished);
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		VanishCommand.getVanished().remove(e.getPlayer());
	}
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e){
		if(VanishCommand.getVanished().contains(e.getPlayer()))
			e.setCancelled(true);
	}
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e){
		if(VanishCommand.getVanished().contains(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
		if(TeleportManager.hasJoinDelayRequest(e.getPlayer().getUniqueId())) {
			Util.sendMsg(e.getPlayer(), " &8» &cNie mozesz wyrzucac itemow tak szybko!");
			e.setCancelled(true);
			return;
		}
		if(TeleportManager.hasTeleportRequest(e.getPlayer().getUniqueId()))
		{
			Util.sendMsg(e.getPlayer(), " &8» &cNie mozesz wyrzucac itemow podczas teleportacji!");
			e.setCancelled(true);
			return;
		}
	}
}
