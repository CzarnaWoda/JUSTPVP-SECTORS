package pl.blackwater.core.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import org.bukkit.event.entity.EntityExplodeEvent;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;

import java.util.regex.Pattern;

public class BlockBreakListener implements Listener{
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		final Block b = e.getBlock();
		if(b.getType() != Material.CHEST)
			return;
		final User u = UserManager.getUser(e.getPlayer());
		if(u.getPlaceChest() > 0)
			u.removePlaceChest(1);
	}
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e){
		e.setCancelled(true);
	}
}
