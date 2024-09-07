package pl.blackwater.core.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.guilds.ranking.RankingManager;

public class TopUpdateTask extends BukkitRunnable{
	
	@Override
	public void run() {
		RankingManager.sortGuildRankings();
		RankingManager.sortUserRankings();
		
	}

}
