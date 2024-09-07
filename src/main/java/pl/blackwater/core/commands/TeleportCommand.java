package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.TeleportManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class TeleportCommand extends PlayerCommand implements Colors
{
    public TeleportCommand() {
        super("teleport", "Teleport do graczy lub koordynaty", "/teleport [kto] <do kogo>  lub  [kto] <x> <y> <z>", "core.teleport", "tp");
    }
    
    public boolean onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 1: {
                final User other = UserManager.getUser(args[0]);
                if (other == null || !other.isOnline()) {
                    return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
                }
                final User u = UserManager.getUser(sender.getUniqueId());
                if(u == null)
                {
                    sender.kickPlayer("Nie ma Cie na liscie gosci!");
                    return false;
                }
                TeleportManager.teleportPlayer(u, other);
                break;
            }
            case 2: {
                if (!sender.hasPermission("core.teleport.others")) {
                    return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Nie masz uprawnien. " + SpecialSigns + "(" + WarningColor + "core.teleport.others" + SpecialSigns + ")");
                }
                final User player = UserManager.getUser(args[0]);
                final User o = UserManager.getUser(args[1]);
                if (player == null || o == null || !o.isOnline() || !player.isOnline()) {
                    return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
                }
                TeleportManager.teleportPlayer(player, o);
                //Util.sendMsg(player, MainColor + "Zostales przeteleportowany do " + ImportantColor + o.getName() + " " + MainColor + "przez " + ImportantColor + sender.getName() + MainColor + "!");
                //Util.sendMsg(sender, MainColor + "Przeteleportowano gracza " + ImportantColor + player.getName() + " " + MainColor + "do gracza " + ImportantColor + o.getName() + MainColor + "!");
                break;
            }
            case 3: {
                if (!Util.isInteger(args[0]) || !Util.isInteger(args[1]) || !Util.isInteger(args[2])) {
                    return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Podana wartosc nie jest liczba!");
                }
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);
                Location l = new Location(sender.getWorld(), (double)x, (double)y, (double)z).add(0.5, 0.5, 0.5);
                sender.teleport(l, PlayerTeleportEvent.TeleportCause.COMMAND);
                Util.sendMsg(sender, MainColor + "Przeteleportowano na koordynaty " + ImportantColor + "{" + x + ", " + y + ", " + z + "}" + MainColor + "!");
                break;
            }
            case 4: {
                if (!sender.hasPermission("core.teleport.others")) {
                    return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Nie masz uprawnien. " + SpecialSigns + "(" + WarningColor + "core.teleport.others" + SpecialSigns + ")");
                }
                Player ot = Bukkit.getPlayer(args[0]);
                if (ot == null) {
                    return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
                }
                if (!Util.isInteger(args[1]) || !Util.isInteger(args[2]) || !Util.isInteger(args[3])) {
                    return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Podana wartosc nie jest liczba!");
                }
                int x2 = Integer.parseInt(args[1]);
                int y2 = Integer.parseInt(args[2]);
                int z2 = Integer.parseInt(args[3]);
                Location l2 = new Location(sender.getWorld(), (double)x2, (double)y2, (double)z2).add(0.5, 0.5, 0.5);
                ot.teleport(l2, PlayerTeleportEvent.TeleportCause.COMMAND);
                Util.sendMsg(ot, MainColor + "Zostales przeteleportowany na koordynaty " + ImportantColor + "{" + x2 + ", " + y2 + ", " + z2 + "}" + MainColor + " przez " + ImportantColor + sender.getName() + MainColor + "!");
                Util.sendMsg(sender, MainColor + "Przeteleportowano gracza " + ImportantColor + ot.getName() + " " + MainColor + "na koordynaty " + ImportantColor + "{" + x2 + ", " + y2 + ", " + z2 + "}" + MainColor + "!");
                break;
            }
            default: {
                Util.sendMsg(sender, MainColor + MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
                break;
            }
        }
        return true;
    }
}
