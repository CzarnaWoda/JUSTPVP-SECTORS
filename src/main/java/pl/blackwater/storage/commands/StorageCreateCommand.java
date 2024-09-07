package pl.blackwater.storage.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.storage.managers.CreatorManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.TimeUtil;
import pl.blackwaterapi.utils.Util;

public class StorageCreateCommand extends PlayerCommand implements Colors{
	
	public StorageCreateCommand() {
		super("storagecreate", "tworzenie magazynu", "/storagecreate <time> <cost>", "storage.create");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length != 2) {
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}
		if(!Util.isInteger(args[1]) || !Util.isInteger(args[0]))
			return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Podana wartosc nie jest liczba!");
		final int cost = Integer.parseInt(args[1]);
		final long time = TimeUtil.DAY.getTime(Integer.parseInt(args[0]));
		if(CreatorManager.getCreator(p) == null) {
			CreatorManager.addCreator(p, null, time, cost, null, false, false,false, null);
		}else
			return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Tworzysz juz magazyn!");
		Util.sendMsg(p, Util.replaceString(SpecialSigns + "1 ->> " + MainColor + "Zaznacz region uzywajac drewniana siekiere"));
		Util.sendMsg(p, Util.replaceString(SpecialSigns + "2 ->> " + MainColor + "Zaznacz EMERALD_BLOCK jako interactBlock prawym klawiszem"));
		Util.sendMsg(p, Util.replaceString(SpecialSigns + "3 ->> " + MainColor + "Zaznacz SIGN jako signBlockState prawym klawiszem"));
		return false;
	}
	

}
