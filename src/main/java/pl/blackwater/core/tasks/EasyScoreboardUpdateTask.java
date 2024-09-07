package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.blackwater.core.managers.EasyScoreboardManager;

public class EasyScoreboardUpdateTask extends BukkitRunnable{
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			EasyScoreboardManager.updateScoreBoard(p);
		}
		
	}

}
