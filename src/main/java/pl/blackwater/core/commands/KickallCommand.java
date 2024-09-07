package pl.blackwater.core.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class KickallCommand extends Command implements Colors
{

	public KickallCommand()
	{
		super("kickall", "wyrzuca wszystkich graczy z serwera", "/kickall", "core.kickall");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		final String reason = Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + UnderLined + "KICKALL" + SpecialSigns + " <<\n " + SpecialSigns + "* " + ImportantColor + BOLD + "JustPvP.PL" + SpecialSigns + " *\n" + StringUtils.join(args, " ")));
		for(Player online : Bukkit.getOnlinePlayers())
		{
			if(!online.hasPermission("core.kickall.bypass")) 
				online.kickPlayer(reason);
		}
		return Util.sendMsg(sender, SpecialSigns + ">> " + MainColor + " Wyrzucono wszystkich graczy z " + ImportantColor + "serwera");
	}

}
