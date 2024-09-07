package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class MoneyCommand extends PlayerCommand implements Colors{

	public MoneyCommand() {
		super("money", "pokazuje status konta gracza", "/money", "core.money", "kasa");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		final User u = UserManager.getUser(p);
		if(u == null)
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
		Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Status twojego konta wynosi: " + ImportantColor + u.getMoney() + "$")));
		return false;
	}

}
