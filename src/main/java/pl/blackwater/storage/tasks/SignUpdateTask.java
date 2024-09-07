package pl.blackwater.storage.tasks;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.storage.data.StorageRent;
import pl.blackwater.storage.managers.StorageRentManager;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;

public class SignUpdateTask extends BukkitRunnable implements Colors{
	
	public void run() {
		if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)) {
			if (Bukkit.getOnlinePlayers().size() > 0) {
				for (StorageRent storage : StorageRentManager.getStoragerents().values()) {
					if (storage.isRent()) {
						final Sign sign = storage.getReSign();
						sign.setLine(0, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wlasciciel ")));
						sign.setLine(1, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ImportantColor + UserManager.getUser(storage.getOwner()).getLastName())));
						sign.setLine(2, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wygasa za")));
						sign.setLine(3, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + Util.secondsToString((int) ((int) ((int) storage.getExpireTime() - System.currentTimeMillis()) / 1000L)))));
						sign.update();
					} else {
						final Sign sign = storage.getReSign();
						if (sign.getLine(0).equalsIgnoreCase(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wlasciciel ")))) {
							storage.getSign().setLine(0, Util.fixColor(Util.replaceString("->> " + MainColor + "DO KUPNA")));
							storage.getSign().setLine(1, Util.fixColor(Util.replaceString("->> " + MainColor + "ID: " + SpecialSigns + storage.getId())));
							storage.getSign().setLine(2, Util.fixColor(Util.replaceString("->> " + MainColor + "Koszt: " + SpecialSigns + storage.getCost() + "$")));
							storage.getSign().setLine(3, Util.fixColor(Util.replaceString("->> " + MainColor + "Na czas: " + SpecialSigns + Util.secondsToString((int) (storage.getRentTime() / 1000)))));
							storage.getSign().update();
						}
					}
				}
			}
		}
		
	}

}
