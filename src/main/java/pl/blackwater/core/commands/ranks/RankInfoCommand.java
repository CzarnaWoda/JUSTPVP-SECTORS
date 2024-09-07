package pl.blackwater.core.commands.ranks;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class RankInfoCommand extends Command implements Colors {
    public RankInfoCommand() {
        super("rankinfo", "Sprawdza informacje na temat rangi", "/rank rankinfo [rank]", "core.rank");
    }

    @Override
    public boolean onExecute(CommandSender sender, String[] args) {
        if(args.length < 1){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}" , getUsage()));
        }
        final Rank r = Core.getRankManager().getRank(args[0]);
        if(r == null){
            return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Taka ranga nie istnieje, sprawdz ranks.yml!");
        }
        if (args.length > 1 && args[1].equalsIgnoreCase("perminfo")){
            return sendRankPermInfo(sender,r);
        }else{
            return sendRankInfo(sender, r);
        }
    }
    public static boolean sendRankInfo(CommandSender sender, Rank r){
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "▬▬ " + ImportantColor + "RANKINFO " + SpecialSigns + ">> " + ImportantColor + r.getName().toUpperCase() + SpecialSigns + " ▬▬")));
        Util.sendMsg(sender, "");
        final int amount = (int) UserManager.getUsers().values().stream().filter(user -> user.getRank().equalsIgnoreCase(r.getName())).count();
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Ilosc osob posiadajacych range: " + ImportantColor + amount)));
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Prefix rangi: " + ImportantColor + r.getPrefix() + sender.getName())));
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Suffix rangi: " + ImportantColor + r.getSuffix() + "Przykladowa wiadomosc o jebaniu BaoBao96")));
        if(sender instanceof Player){
            final Player p = (Player)sender;
            Util.SendRun_CommandTextComponent(p, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Sprawdz uprawnienia rangi!")), "/rank rankinfo " + r.getName() + " perminfo", Util.fixColor(Util.replaceString(SpecialSigns + ">> " + WarningColor + "Kliknij aby zobaczyc uprawnienia rangi!")));
        }
        Util.sendMsg(sender, "");
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "▬▬ " + ImportantColor + "RANKINFO " + SpecialSigns + ">> " + ImportantColor + r.getName().toUpperCase() + SpecialSigns + " ▬▬")));
        return true;
    }
    public static boolean sendRankPermInfo(CommandSender sender, Rank r){
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "▬▬ " + ImportantColor + "RANKINFO " + SpecialSigns + ">> " + ImportantColor + r.getName().toUpperCase() + SpecialSigns + " ▬▬")));
        Util.sendMsg(sender, "");
        r.getPermissions().forEach(permission -> Util.sendMsg(sender, Util.replaceString(SpecialSigns + "->> " + ChatColor.GREEN + "%V% " + permission)));
        Util.sendMsg(sender, "");
        Util.sendMsg(sender, Util.fixColor(Util.replaceString(SpecialSigns + "▬▬ " + ImportantColor + "RANKINFO " + SpecialSigns + ">> " + ImportantColor + r.getName().toUpperCase() + SpecialSigns + " ▬▬")));
        return true;
    }
}
