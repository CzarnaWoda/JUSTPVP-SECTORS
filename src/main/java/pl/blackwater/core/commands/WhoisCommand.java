package pl.blackwater.core.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class WhoisCommand extends Command implements Colors{

	public WhoisCommand() {
        super("whois", "informacje o graczu", "/whois <player>", "core.whois");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args.length != 1)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		User u = UserManager.getUser(args[0]);
		if (u == null)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
		OfflinePlayer p = u.getOfflinePlayer();
		//Ban b = BanManager.getBan(u);
		//BanIP bip = BanIPManager.getBanIP(u);
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + ">> " + MainColor + "Informacje o graczu " + SpecialSigns + "* " + WarningColor + u.getLastName() + SpecialSigns + " <<"));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Online: " + (p.isOnline() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + (p.isOnline() ? "Online od: " + ImportantColor + Util.secondsToString((int)((int)(System.currentTimeMillis() - u.getLastJoin()) / 1000L)) : "Ostatnie dolaczenie na serwer: " + ImportantColor + Util.getDate(u.getLastJoin()))));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Operator: " + (p.isOp() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Gamemode: " + u.getGameMode().toString().toUpperCase()));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Latanie: " + (u.isFly() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Godmode: " + (u.isGod() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Lokacja: " +  ImportantColor + LocationUtil.convertLocationToString(u.getLastLocation())));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Lokacja home: " + ImportantColor + LocationUtil.convertLocationToString(u.getHomeLocation())));
		//Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Zbanowany: " + ImportantColor + ((b != null) ? (((b.getExpireTime() == 0L) ? "permamentnie" : "tymczasowo (wygasa " + Util.getDate(b.getExpireTime()) + ")") + MainColor + ", powod: " + ImportantColor + b.getReason() + MainColor + ", przez: " + ImportantColor + b.getAdmin()) : ChatColor.DARK_RED + "%X%")));
		//TODO Util.sendMsg(sender, Util.replaceString(SpecialSigns + "  * " + MainColor + "Zbanowany IP: " + ImportantColor + ((bip != null) ? (((bip.getExpireTime() == 0L) ? "permamentnie" : "tymczasowo (wygasa " + Util.getDate(bip.getExpireTime()) + ")") + MainColor + ", powod: " + ImportantColor + bip.getReason() + MainColor + ", przez: " + ImportantColor + bip.getAdmin()) : ChatColor.DARK_RED + "%X%")));
		Util.sendMsg(sender, Util.replaceString(SpecialSigns + ">> " + MainColor + "Informacje o graczu " + SpecialSigns + "* " + WarningColor + u.getLastName() + SpecialSigns + " <<"));
		return false;
	}

}
