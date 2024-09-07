package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.blackwater.core.data.Combat;
import pl.blackwater.core.managers.CombatManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.Util;

public class CombatActionBarTask extends BukkitRunnable{
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			final Combat c = CombatManager.getCombat(p);
			if(c != null && c.hasFight()) {
				final long secondsLeft = (c.getLastAttactTime() / 1000) - (System.currentTimeMillis() / 1000);
				if(secondsLeft >= 0) {
					ActionBarUtil.sendActionBar(p, Util.fixColor(MessageConfig.ANTYLOGOUTACTIONBARMESSAGE_INFIGHT).replace("{SECOND}", String.valueOf(secondsLeft)));
				}
			}
		}
	}

}
