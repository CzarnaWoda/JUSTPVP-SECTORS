package pl.blackwater.core.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class SetHomeCommand extends PlayerCommand implements Colors{

	public SetHomeCommand() {
		super("sethome", "ustawia lokacje domu", "/sethome", "core.sethome");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		Location loc = p.getLocation();
		User u = UserManager.getUser(p);
		u.setHomeLocation(loc);
		return Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ustawiles lokalizacje domu na kordynatach: " + ImportantColor + LocationUtil.convertLocationToString(loc))));
	}

}
