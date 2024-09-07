package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.blackwater.core.data.Combat;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.CombatManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.Util;

public class CombatEndTask extends BukkitRunnable implements Colors{
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			final Combat c = CombatManager.getCombat(p);
			if(c != null) {
				if(c.wasFight() && !c.hasFight()) {
					c.setLastAttactkPlayer(null);
					Util.sendMsg(p, Util.fixColor(MessageConfig.ANTYLOGOUTMESSAGE_ENDFIGHT));
					ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(MessageConfig.ANTYLOGOUTACTIONBARRMESSAGE_ENDFIGHT)));
				}
			}
		}
	}

}
