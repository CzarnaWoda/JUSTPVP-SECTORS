package pl.blackwater.core.commands.ranks;

import org.bukkit.command.CommandSender;
import pl.blackwater.core.data.RankSet;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.RankSetManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class RemoveCommand extends Command implements Colors {
    public RemoveCommand() {
        super("remove", "Usuwa daną range graczowi!", "/rank remove [player]", "core.rank");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}" , getUsage()));
        }
        User o = UserManager.getUser(args[0]);
        if(o == null){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
        }
        final RankSet rankSet = RankSetManager.getSetRank(o);
        if(rankSet == null){
            return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Ten gracz ma standardowa range!");
        }
        RankSetManager.removeRank(o,RankSetManager.getSetRank(o));
        return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor  + "RankManager " + SpecialSigns + "->> " + MainColor + "Usunieto range " + ImportantColor + rankSet.getRank() + MainColor + " dla gracza " + ImportantColor + o.getLastName())));
    }
}
