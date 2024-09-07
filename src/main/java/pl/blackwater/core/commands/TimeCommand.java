package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class TimeCommand extends Command implements Colors{

	public TimeCommand() {
		super("time", "zmienia czas na serwerze", "/time <godzina>", "core.time");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args.length != 1)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		if(!Util.isInteger(args[0]))
			return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Podana wartosc nie jest liczba!");
		final int value = Integer.parseInt(args[0]);
		if(value > 24 || value < 0)
			return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Wartosc musi byc pomiedzy 0 a 24");
		World w;
		if(sender instanceof Player) {
			Player p = (Player)sender;
			w = p.getWorld();
		}else
			w = Bukkit.getWorlds().get(0);
		w.setTime((long)value*1000);
		return Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ustawiles czas na swiecie na " + ImportantColor + value + "h " + MainColor + "!")));
	}

}
