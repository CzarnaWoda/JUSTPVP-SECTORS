package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.money.PayTransactionPacket;
import pl.justsectors.redis.client.RedisClient;

public class PayCommand extends PlayerCommand implements Colors
{

	public PayCommand()
	{
		super("pay", "przelewa pieniadze na konto gracza", "/pay <nick> <ilosc>", "core.pay", "przelej","zaplac");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length != 2) 
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		User ou = UserManager.getUser(args[0]);
		if(ou == null) 
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
		if(!ou.isOnline())
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
		if(!Util.isInteger(args[1]))
			return Util.sendMsg(p, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Podana wartosc nie jest liczba");
		int value = Integer.parseInt(args[1]);
		if(value < 0)
			return Util.sendMsg(p, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Ilosc musi byc wieksza od zera");
		User u = UserManager.getUser(p);
		if(u.getMoney() < value)
			return Util.sendMsg(p, WarningColor + "Blad" + SpecialSigns  + ": " + WarningColor_2 + "Nie posiadasz wystarczajacej ilosci pieniedzy");
		RedisClient.sendSectorsPacket(new PayTransactionPacket(u.getLastName(), ou.getLastName(), value));
		return false;
	}

}
