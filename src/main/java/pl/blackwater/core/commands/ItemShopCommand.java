package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.blackwater.chestpvpdrop.settings.Config;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.data.User;
import pl.blackwater.core.enums.ChatControlType;
import pl.blackwater.core.managers.ChatControlUserManager;
import pl.blackwater.core.managers.EventManager;
import pl.blackwater.core.managers.RankSetManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.chat.ChatControlGlobalMessagePacket;
import pl.justsectors.packets.impl.others.ItemShopPacket;
import pl.justsectors.redis.client.RedisClient;

public class ItemShopCommand extends Command{

	public ItemShopCommand() {
		super("itemshop", "Komenda do itemshopu", "/itemshop [gracz] [usluga]", "core.itemshop", "is");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args.length != 2) {
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}
		final User u = UserManager.getUser(args[0]);
		if(u == null) {
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
		}
		switch (args[1]) {
		case "vip":
			final ItemShopPacket p = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p);
			return sendItemShopMessage("vip", u.getLastName());
		case "svip":
			final ItemShopPacket p1 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p1);
			return sendItemShopMessage("svip", u.getLastName());
		case "chestpvp10":
			final ItemShopPacket p2 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p2);
			return sendItemShopMessage(Util.replaceString("Klucze ChestPvP * 10"), u.getLastName());
		case "startpack5":
			final ItemShopPacket p3 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p3);
			return sendItemShopMessage(Util.replaceString("Klucze StartPack * 5"), u.getLastName());
		case "minechest10":
			final ItemShopPacket p4 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p4);
			return sendItemShopMessage(Util.replaceString("Klucze MineKey * 10"), u.getLastName());
		case "money10":
			final ItemShopPacket p5 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p5);
			return sendItemShopMessage(Util.replaceString("KASA >> 10000"), u.getLastName());
		case "money25":
			final ItemShopPacket p6 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p6);
			return sendItemShopMessage(Util.replaceString("KASA >> 25000"), u.getLastName());
		case "money50":
			final ItemShopPacket p7 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p7);
			return sendItemShopMessage(Util.replaceString("KASA >> 50000"), u.getLastName());
		case "turbomoney2h":
			final ItemShopPacket p8 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p8);
			return sendItemShopMessage(Util.replaceString("TURBOMONEY >> 2h"), u.getLastName());
		case "turbodropexp1h":
			final ItemShopPacket p9 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p9);
			return sendItemShopMessage(Util.replaceString("TURBODROP,TURBOEXP >> 1h"), u.getLastName());
		case "dropx21h":
			final ItemShopPacket p10 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p10);
			return sendItemShopMessage(Util.replaceString("GLOBALNY DROP x2 >> 1h"), u.getLastName());
		case "expx21h":
			final ItemShopPacket p11 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p11);
			return sendItemShopMessage(Util.replaceString("GLOBALNY EXP x2 >> 1h"), u.getLastName());
		case "chestpvp10startpack10":
			final ItemShopPacket p12 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p12);
			return sendItemShopMessage(Util.replaceString("CHESTPVP * 10 | StartPack * 10 "), u.getLastName());
		case "minechest20startpack5":
			final ItemShopPacket p13 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p13);
			return sendItemShopMessage(Util.replaceString("MINECHEST * 20 | StartPack * 5 "), u.getLastName());
		case "startpack15":
			final ItemShopPacket p14 = new ItemShopPacket(u.getLastName(),args[1]);
			RedisClient.sendSectorsPacket(p14);
			return sendItemShopMessage(Util.replaceString("STARTPACK * 15 "), u.getLastName());
		default:
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}
	}
	private static boolean sendItemShopMessage(String offer, String playerName) {
		final ChatControlGlobalMessagePacket packet = new ChatControlGlobalMessagePacket(Util.fixColor(Util.replaceString("&8->> &7Gracz &2" + playerName + " &7zakupil oferte &2" + offer.toUpperCase())), ChatControlType.ITEMSHOP);
		final ChatControlGlobalMessagePacket packet1 = new ChatControlGlobalMessagePacket(Util.fixColor(Util.replaceString("&8->> &7Dziekujemy za wsparcie &2" + playerName)), ChatControlType.ITEMSHOP);

		RedisClient.sendSectorsPacket(packet);
		RedisClient.sendSectorsPacket(packet1);
		return true;
	}
}
