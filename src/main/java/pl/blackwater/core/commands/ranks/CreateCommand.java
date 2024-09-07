package pl.blackwater.core.commands.ranks;

import org.bukkit.command.CommandSender;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.ranks.CreateRankPacket;
import pl.justsectors.redis.client.RedisClient;

public class CreateCommand extends Command implements Colors {
    public CreateCommand() {
        super("create", "Tworzy nową range!", "/rank create [rank]", "core.rank");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        Rank rank = Core.getRankManager().getRank(args[0]);
        if(rank == null){
            final CreateRankPacket packet = new CreateRankPacket(args[0]);
            RedisClient.sendSectorsPacket(packet);
            return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor  + "RankManager " + SpecialSigns + "->> " + MainColor + "Stworzono range " + ImportantColor + args[0] + SpecialSigns + " (" + WarningColor + "Prefix and Suffix set to 'CHANGE' = DEFAULT value " + SpecialSigns + ")")));
        }else{
            return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Taka ranga juz istnieje!");
        }
    }
}
