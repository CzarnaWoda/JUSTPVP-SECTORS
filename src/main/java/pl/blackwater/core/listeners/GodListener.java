package pl.blackwater.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;

public class GodListener implements Listener{
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if(e.getEntity() instanceof Player) {
			final Player p = (Player) e.getEntity();
			User u = UserManager.getUser(p);
			if(u.isGod())
				e.setCancelled(true);
		}
	}

}
