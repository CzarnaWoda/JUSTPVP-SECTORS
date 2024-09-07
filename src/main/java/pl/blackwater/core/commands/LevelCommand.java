package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.MathUtil;
import pl.blackwaterapi.utils.Util;

public class LevelCommand extends PlayerCommand implements Colors{

	public LevelCommand() {
		super("level", "level", "/level", "core.level", "lvl", "exp", "levels", "doswiadczenie", "levels");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length == 0)
			return levelInfo(p, UserManager.getUser(p));
		else {
			User other = UserManager.getUser(args[0]);
			if(other == null)
				return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
			return levelInfo(p, other);
		}
	}
	
	private static boolean levelInfo(final Player viewer,final User other) {
		Util.sendMsg(viewer, Util.replaceString(SpecialSigns + ">> " + MainColor + "Informacje na temat level'u " + ImportantColor + other.getLastName() + SpecialSigns + " <<"));
		Util.sendMsg(viewer, Util.replaceString(SpecialSigns + "  * " + MainColor + "Level: " + ImportantColor + other.getLevel()));
		Util.sendMsg(viewer, Util.replaceString(SpecialSigns + "  * " + MainColor + "EXP: " + WarningColor + MathUtil.round(other.getExp(), 1) + MainColor + "/" + ImportantColor + MathUtil.round(other.getExpToLevel(), 1)));
		Util.sendMsg(viewer, Util.replaceString(SpecialSigns + "  * " + MainColor + "Postep: " + ImportantColor + MathUtil.round(other.getExp() /other.getExpToLevel()*100.0, 1) + MainColor + "%"));
		Util.sendMsg(viewer, Util.replaceString(SpecialSigns + ">> " + MainColor + "Informacje na temat level'u " + ImportantColor + other.getLastName() + SpecialSigns + " <<"));
		return true;
	}

}
