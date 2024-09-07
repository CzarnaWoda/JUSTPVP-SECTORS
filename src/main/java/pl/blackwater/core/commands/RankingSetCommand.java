package pl.blackwater.core.commands;

import org.bukkit.command.CommandSender;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.user.SetStatisticPacket;
import pl.justsectors.redis.client.RedisClient;

public class RankingSetCommand extends Command implements Colors{
	
	
	public RankingSetCommand() {
        super("rankingset", "ustawianie rankingu gracza", "/rankingset <gracz> <kills|deaths|logouts|killstreak|level|exp|timeplay|money|minestonelimit|breakstone> <wartosc>", "core.rankingset");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args.length != 3)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		if(!Util.isInteger(args[2]))
			return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Podana wartosc nie jest liczba!");
		final int value = Integer.parseInt(args[2]);
		final User u = UserManager.getUser(args[0]);
		if (u == null)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
		final String message = Util.replaceString(">> " + MainColor + "Wartosc " + ImportantColor + args[1].toUpperCase() + MainColor + " zostala ustawiona na wartosc: " + ImportantColor + value);
		switch (args[1]) {
		case "kills":
			final SetStatisticPacket packet9 = new SetStatisticPacket(u.getUuid(),value,"kills");
			RedisClient.sendSectorsPacket(packet9);
			return Util.sendMsg(sender, message);
		case "deaths":
			final SetStatisticPacket packet = new SetStatisticPacket(u.getUuid(),value,"deaths");
			RedisClient.sendSectorsPacket(packet);
			return Util.sendMsg(sender, message);
		case "logouts":
			final SetStatisticPacket packet1 = new SetStatisticPacket(u.getUuid(),value,"logouts");
			RedisClient.sendSectorsPacket(packet1);
			return Util.sendMsg(sender, message);
		case "killstreak":
			final SetStatisticPacket packet2 = new SetStatisticPacket(u.getUuid(),value,"killstreak");
			RedisClient.sendSectorsPacket(packet2);
			return Util.sendMsg(sender, message);
		case "level":
			final SetStatisticPacket packet3 = new SetStatisticPacket(u.getUuid(),value,"level");
			RedisClient.sendSectorsPacket(packet3);
			return Util.sendMsg(sender, message);
		case "exp":
			final SetStatisticPacket packet4 = new SetStatisticPacket(u.getUuid(),value,"exp");
			RedisClient.sendSectorsPacket(packet4);
			return Util.sendMsg(sender, message);
		case "timeplay":
			final SetStatisticPacket packet5 = new SetStatisticPacket(u.getUuid(),value,"timeplay");
			RedisClient.sendSectorsPacket(packet5);
			return Util.sendMsg(sender, message);
		case "money":
			final SetStatisticPacket packet6 = new SetStatisticPacket(u.getUuid(),value,"money");
			RedisClient.sendSectorsPacket(packet6);
			return Util.sendMsg(sender, message);
		case "minestonelimit":
			final SetStatisticPacket packet7 = new SetStatisticPacket(u.getUuid(),value,"minestonelimit");
			RedisClient.sendSectorsPacket(packet7);
			return Util.sendMsg(sender, message);
		case "breakstone":
			final SetStatisticPacket packet8 = new SetStatisticPacket(u.getUuid(),value,"breakstone");
			RedisClient.sendSectorsPacket(packet8);
			return Util.sendMsg(sender, message);
		default:
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}
	}

}
