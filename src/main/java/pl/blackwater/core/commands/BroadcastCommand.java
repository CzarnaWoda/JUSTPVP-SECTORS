package pl.blackwater.core.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.chat.ChatMessagePacket;
import pl.justsectors.redis.client.RedisClient;

public class BroadcastCommand extends Command implements Colors
{
    public BroadcastCommand() {
        super("broadcast", "ogloszenie do graczy", "/broadcast <wiadomosc>", "core.broadcast", "bc", "bcast");
    }
    
    public boolean onExecute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        final ChatMessagePacket packet = new ChatMessagePacket(Util.fixColor(Util.replaceString("" + SpecialSigns + BOLD + ">> " + ImportantColor + UnderLined + "Ogloszenie " + SpecialSigns + BOLD + "<< " + MainColor + StringUtils.join(args," "))));
        RedisClient.sendSectorsPacket(packet);
        return false;
    }
    //StringUtils.join((Object[])args, " "
}
