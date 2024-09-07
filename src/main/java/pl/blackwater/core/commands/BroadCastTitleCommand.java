package pl.blackwater.core.commands;


import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.others.SendTitlePacket;
import pl.justsectors.redis.client.RedisClient;

public class BroadCastTitleCommand extends PlayerCommand{

	public BroadCastTitleCommand() {
		super("titlebc", "broadcast", "/titlebc [naglowek] [tekst]", "core.titlebc");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(Player p, String[] args) {
        if (args.length < 2) {
            return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        final SendTitlePacket titlePacket = new SendTitlePacket(Util.fixColor(args[0].replaceAll("_", " ")),StringUtils.join(args," ",1,args.length));
        RedisClient.sendSectorsPacket(titlePacket);
        return false;
	}

}
