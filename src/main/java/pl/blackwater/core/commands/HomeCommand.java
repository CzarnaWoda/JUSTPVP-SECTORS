package pl.blackwater.core.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.timer.TimerUtil;
import pl.blackwaterapi.utils.Util;

public class HomeCommand extends PlayerCommand implements Colors
{
    
    public HomeCommand() {
        super("home", "dom", "/home", "core.home");
    }
    
	public boolean onCommand(Player sender, String[] args) {
        if (args.length == 0) {
            Location loc = null;
            User user = UserManager.getUser(sender.getUniqueId());
            loc = user.getHomeLocation();
            if (loc == null) {
                Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Nie ustawiles lokacji domu!");
                return false;
            }
            TimerUtil.teleport(sender, loc, 10);
        }
        return false;
    }
}
