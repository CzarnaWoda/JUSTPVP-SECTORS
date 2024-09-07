package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
public class GodCommand extends PlayerCommand implements Colors
{
    public GodCommand() {
        super("god", "zarzadzanie trybem goda graczy", "/god [gracz]", "core.god");
    }
    
    public boolean onCommand(Player sender, String[] args) {
        if (args.length == 1) {
            if (!sender.hasPermission("core.god.others")) {
                return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Nie posiadasz uprawnien " + SpecialSigns + "(" + WarningColor + "core.god.others" + SpecialSigns + ")");
            }
            Player o = Bukkit.getPlayer(args[0]);
            if (o == null) {
                return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + " &6Gracz nie jest online!");
            }
            User u = UserManager.getUser(o);
            if (u == null) {
                return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + " &6Uzytkownik nie istnieje w bazie danych!");
            }
            u.setGod(!u.isGod());
            Util.sendMsg(sender, ImportantColor + (u.isGod() ? "Wlaczono" : "Wylaczono") + MainColor + "tryb goda dla gracza " + ImportantColor + u.getLastName() + MainColor + "!");
            return Util.sendMsg(o, ImportantColor + (u.isGod() ? "Wlaczono" : "Wylaczono") + MainColor + "tryb goda przez " + ImportantColor + sender.getName() + MainColor + "!");
        }
        else {
            User u2 = UserManager.getUser(sender);
            if (u2 == null) {
                return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Uzytkownik nie istnieje w bazie danych!");
            }
            u2.setGod(!u2.isGod());
            return Util.sendMsg(sender, ImportantColor + (u2.isGod() ? "Wlaczono" : "Wylaczono") + MainColor + " tryb goda!");
        }
    }
}
