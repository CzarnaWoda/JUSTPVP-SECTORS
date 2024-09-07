package pl.blackwater.core.commands;

import org.bukkit.command.CommandSender;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.user.AddStatisticPacket;
import pl.justsectors.redis.client.RedisClient;

public class AddStatisticCommand extends Command implements Colors
{

	public AddStatisticCommand()
	{
		super("addstatistics", "dodaje statystyki do gracza", "/addstatistics <player> [money/level/exp/kills] <amount>", "core.addstatistics", "addstat");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args.length != 3)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		final User u = UserManager.getUser(args[0]);
		if(u == null) return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
		if(!Util.isInteger(args[2])) return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Podana wartosc nie jest liczba!");
		final int value = Integer.parseInt(args[2]);
		final String message = MainColor + "Dodano " + ImportantColor + UnderLined + value + MainColor + " do wartosci " + ImportantColor + UnderLined + args[1].toLowerCase() + MainColor + " dla gracza " + ImportantColor + u.getLastName();
		switch (args[1]) {
		case "money":
			final AddStatisticPacket packet = new AddStatisticPacket(u.getUuid(),"money",value);
			RedisClient.sendSectorsPacket(packet);
			Util.sendMsg(sender, message);
			break;
		case "level":
			final AddStatisticPacket packet1 = new AddStatisticPacket(u.getUuid(),"level",value);
			RedisClient.sendSectorsPacket(packet1);
			Util.sendMsg(sender, message);
			break;
		case "exp":
			final AddStatisticPacket packet2 = new AddStatisticPacket(u.getUuid(),"exp",value);
			RedisClient.sendSectorsPacket(packet2);
			Util.sendMsg(sender, message);
			break;
		case "kills":
			final AddStatisticPacket packet3 = new AddStatisticPacket(u.getUuid(),"kills",value);
			RedisClient.sendSectorsPacket(packet3);
			Util.sendMsg(sender, message);
			break;
		default:
			Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
			break;
		}
		return false;
	}

}
