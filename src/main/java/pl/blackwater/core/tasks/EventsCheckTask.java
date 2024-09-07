package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import pl.blackwater.chestpvpdrop.settings.Config;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.EventManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.utils.Util;

public class EventsCheckTask extends BukkitRunnable implements Colors{
	
	@Override
	public void run() {
		for(final Player p : Bukkit.getOnlinePlayers()) {
			final User u = UserManager.getUser(p);
			if(System.currentTimeMillis() >= u.getEventTurboMoneyTime() && u.getEventTurboMoney() == 2) {
				Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czas eventu " + ImportantColor + "TurboMoney " + SpecialSigns + "(" + ImportantColor +  UnderLined + "Tryb Gracz" + SpecialSigns + ") " + MainColor + "skonczyl sie!")));
				u.setEventTurboMoney(1);
			}
			if(System.currentTimeMillis() >= u.getTurboDropTime() && u.isTurboDrop()) {
				Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czas eventu " + ImportantColor + "TurboDrop " + SpecialSigns + "(" + ImportantColor +  UnderLined + "Tryb Gracz" + SpecialSigns + ") " + MainColor + "skonczyl sie!")));
				u.setTurboDrop(false);
			}
			if(System.currentTimeMillis() >= u.getTurboExpTime() && u.isTurboExp()) {
				Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czas eventu " + ImportantColor + "TurboEXP " + SpecialSigns + "(" + ImportantColor +  UnderLined + "Tryb Gracz" + SpecialSigns + ") " + MainColor + "skonczyl sie!")));
				u.setTurboExp(false);
			}
			if(!EventManager.isOnEventDrop()) {
				if(EventManager.getTurbodrop().containsKey(1) && System.currentTimeMillis() >= EventManager.getTurbodrop_time()) {
					Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czas eventu " + ImportantColor + "TurboDrop " + SpecialSigns + "(" + ImportantColor +  UnderLined + "Tryb ALL" + SpecialSigns + ") " + MainColor + "skonczyl sie!")));
					EventManager.getTurbodrop().clear();
				}
			}
			if(!EventManager.isOnEventExp()) {
				if(EventManager.getTurboexp().containsKey(1) && System.currentTimeMillis() >= EventManager.getTurboexp_time()) {
					Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czas eventu " + ImportantColor + "TurboEXP " + SpecialSigns + "(" + ImportantColor +  UnderLined + "Tryb ALL" + SpecialSigns + ") " + MainColor + "skonczyl sie!")));
					EventManager.getTurboexp().clear();
				}
			}
			if(!EventManager.isOnEventInfinityStone()) {
				if(EventManager.getInfinitystone().containsKey(1) && System.currentTimeMillis() >= EventManager.getInfinitystone_time()) {
					Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czas eventu " + ImportantColor + "InfinityStone " + SpecialSigns + "(" + ImportantColor +  UnderLined + "Tryb ALL" + SpecialSigns + ") " + MainColor + "skonczyl sie!")));
					EventManager.getInfinitystone().clear();
				}
			}
			if(!EventManager.isOnEventDrop()) {
				if(EventManager.getDrop().containsKey(1) && System.currentTimeMillis() >= EventManager.getDrop_time()) {
					Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czas eventu " + ImportantColor + "Drop x" + Config.EVENT_DROP + SpecialSigns + " (" + ImportantColor +  UnderLined + "Tryb ALL" + SpecialSigns + ") " + MainColor + "skonczyl sie!")));
					EventManager.getDrop().clear();
				}
			}
			if(!EventManager.isOnEventExp()) {
				if(EventManager.getExp().containsKey(1) && System.currentTimeMillis() >= EventManager.getExp_time()) {
					Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czas eventu " + ImportantColor + "EXP x" + Config.EVENT_EXP + SpecialSigns + " (" + ImportantColor +  UnderLined + "Tryb ALL" + SpecialSigns + ") " + MainColor + "skonczyl sie!")));
					EventManager.getExp().clear();
				}
			}
		}
	}

}
