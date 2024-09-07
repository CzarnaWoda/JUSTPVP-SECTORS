package pl.blackwater.core.commands.ranks;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.data.RankSet;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.RankSetManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class SetCommand extends Command implements Colors {
    public SetCommand() {
        super("set", "Nadaje daną range na dany czas lub na zawsze!", "/rank set [player] [rank] [(optional)time]", "core.rank");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(args.length < 2){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}" , getUsage()));
        }
        final Player o = Bukkit.getPlayer(args[0]);
        if(o == null){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        }
        final User user = UserManager.getUser(o);
        if(user == null){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
        }
        final Rank rank = Core.getRankManager().getRank(args[1]);
        if(rank == null){
            return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Taka ranga nie istnieje, sprawdz ranks.yml!");
        }
        final RankSet rankSet = RankSetManager.getSetRank(user);
        if(rankSet != null){
            return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Gracz posiada juz nadaną range, najpierw usun ją za pomocą /rank remove [player] !");
        }
        long time;
        if(args.length == 3){
            time = Util.parseDateDiff(args[2], true);
        }else{
            time = 0L;
        }
        RankSetManager.setRank(user, sender.getName(), rank, time);
        return Util.sendMsg(sender, Util.fixColor(Util.replaceString(WarningColor  + "RankManager " + SpecialSigns + "->> " + MainColor + "Nadano range " + ImportantColor + rank.getName() + MainColor + " dla gracza " + ImportantColor + o.getName() + SpecialSigns + " (" + WarningColor_2  + (time == 0L ? "na zawsze" : "na " + Util.secondsToString((int) ((time-System.currentTimeMillis())/1000L)) + ", do " + Util.getDate(time)) + SpecialSigns + ")")));
    }
}
