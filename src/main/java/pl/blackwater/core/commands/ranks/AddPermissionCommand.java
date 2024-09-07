package pl.blackwater.core.commands.ranks;

import org.bukkit.command.CommandSender;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.ranks.AddPermissionPacket;
import pl.justsectors.redis.client.RedisClient;

public class AddPermissionCommand extends Command implements Colors {
    public AddPermissionCommand() {
        super("addpermission", "Dodaje uprawienie do danej rangi", "/rank addpermission [rank] [permission]", "core.rank");
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
        final AddPermissionPacket permissionPacket = new AddPermissionPacket(r.getName(),args[1]);
        RedisClient.sendSectorsPacket(permissionPacket);
        return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor  + "RankManager " + SpecialSigns + "->> " + MainColor + "Dodano uprawienie " + ImportantColor +  args[1] + MainColor + " dla rangi " + ImportantColor + r.getName())));
    }
}
