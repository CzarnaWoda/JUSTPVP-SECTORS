package pl.blackwater.core.commands.ranks;

import org.bukkit.command.CommandSender;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.ranks.RemovePermissionPacket;
import pl.justsectors.redis.client.RedisClient;

public class RemovePermissionCommand extends Command implements Colors {
    public RemovePermissionCommand() {
        super("removepermission", "Usuwa uprawienie dla danej rangi!", "/rank removepermission [rank] [permission]", "core.rank");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(args.length != 2){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        Rank r = Core.getRankManager().getRank(args[0]);
        if(r == null){
            return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Taka ranga nie istnieje, sprawdz ranks.yml!");
        }
        final RemovePermissionPacket packet = new RemovePermissionPacket(r.getName(),args[1]);
        RedisClient.sendSectorsPacket(packet);
        return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor  + "RankManager " + SpecialSigns + "->> " + MainColor + "Usunieto uprawienie " + ImportantColor +  args[1] + MainColor + " dla rangi " + ImportantColor + r.getName())));
    }
}
