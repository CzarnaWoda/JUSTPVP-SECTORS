package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.managers.ChatControlUserManager;
import pl.blackwater.core.settings.MessageConfig;

public class AutoMessageTask extends BukkitRunnable{
	private int index;
	
	public AutoMessageTask() {
		this.index = 0;
	}
	@Override
	public void run() {
		if(index >= MessageConfig.AUTOMESSAGE_MESSAGES.size()) {
			index = 0;
		}
		for(Player p : Bukkit.getOnlinePlayers()) {
			ChatControlUser cc = ChatControlUserManager.getUser(p);
			ChatControlUserManager.sendMsg(p, MessageConfig.AUTOMESSAGE_PREFIX + MessageConfig.AUTOMESSAGE_MESSAGES.get(index), cc.isAutoMessage());
		}
		index++;
	}

}
