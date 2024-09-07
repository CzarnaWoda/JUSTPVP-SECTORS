package pl.blackwater.core.commands;

import org.bukkit.command.CommandSender;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.user.RankingResetPacket;
import pl.justsectors.redis.client.RedisClient;

public class RankingResetCommand extends Command implements Colors{

	public RankingResetCommand() {
        super("resetranking", "resetowanie rankingu gracza", "/rankingreset <gracz>", "core.rankingreset", "resetelo");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args.length == 0)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		final User u = UserManager.getUser(args[0]);
		if(u == null)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);

		RedisClient.sendSectorsPacket(new RankingResetPacket(sender.getName(), u.getLastName()));
		return true;
	}

}
