package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class TeleportHereCommand extends PlayerCommand implements Colors{

	public TeleportHereCommand() {
		super("teleporthere", "teleportacja gracza do siebie", "/teleporthere [player]", "core.teleporthere", "stp","tphere");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length != 1) 
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		final Player other = Bukkit.getPlayer(args[0]);
		if(other == null)
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
		other.teleport(p.getLocation());
        Util.sendMsg(other, MainColor + "Zostales przeteleportowany do " + ImportantColor + p.getName() + MainColor + " !");
        Util.sendMsg(p, MainColor + "Przeteleportowano gracza " + ImportantColor + other.getName() + " " + MainColor + "do gracza " + ImportantColor + p.getName() + MainColor + "!");
		return false;
	}
	

}
