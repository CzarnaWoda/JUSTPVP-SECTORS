package pl.blackwater.core.commands.ranks;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.RankSet;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.RankSetManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class UserInfoCommand extends Command implements Colors {
    public UserInfoCommand() {
        super("userinfo", "Sprawdza informacje o randze na temat gracz'a", "/rank userinfo [player]", "core.rank");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        final User o = UserManager.getUser(args[0]);
        if(o == null){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
        }
        sendUserInfo(sender,o, RankSetManager.getSetRank(o));
        return false;
    }
    private static void sendUserInfo(CommandSender sender, User u, RankSet rankSet){
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "▬▬ " + ImportantColor + "USERINFO " + SpecialSigns + ">> " + ImportantColor + u.getLastName() + SpecialSigns + " ▬▬")));
        Util.sendMsg(sender, "");

        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Aktualna ranga: " + ImportantColor + u.getRank() + (rankSet == null ? ", na zawsze" : (rankSet.getExpireTime() == 0L ? ", na zawsze" : ", do " + Util.getDate(rankSet.getExpireTime()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int) ((rankSet.getExpireTime()-System.currentTimeMillis())/1000L)) + SpecialSigns + ")")))));
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Nadana przez: " + ImportantColor + (rankSet == null ? "Konsole" : rankSet.getAdmin()))));
        if(sender instanceof Player) {
            final Player p = (Player)sender;
            Util.SendRun_CommandTextComponent(p,Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "SPRAWDZ INFORMACJE NA TEMAT RANGI GRACZA!")),"/rank rankinfo " + u.getRank(), Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Kliknij aby sprawdzic informacje na temat rangi " + ImportantColor + u.getRank())));
        }
        Util.sendMsg(sender, "");
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "▬▬ " + ImportantColor + "USERINFO " + SpecialSigns + ">> " + ImportantColor + u.getLastName() + SpecialSigns + " ▬▬")));
    }
}
