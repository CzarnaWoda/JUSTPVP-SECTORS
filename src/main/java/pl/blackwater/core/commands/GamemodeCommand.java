package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class GamemodeCommand extends Command implements Colors
{
    public GamemodeCommand() {
        super("gamemode", "Zmiana trybu gry graczy", "/gamemode [gracz] <tryb>", "core.gamemode", "gm", "gmode");
    }
    
    static void setGamemode(Player p, GameMode mode, CommandSender changer) {
        User u = UserManager.getUser(p.getUniqueId());
        if (u == null) {
            return;
        }
        p.setGameMode(mode);
        p.setAllowFlight(mode.equals(GameMode.CREATIVE));
        u.setGameMode(mode);
        u.setFly(mode.equals(GameMode.CREATIVE));
        u.setGod(mode.equals(GameMode.CREATIVE));
        if (changer == null) {
            Util.sendMsg(p, MainColor + "Twoj tryb gry zostal zmieniony na " + ImportantColor + mode.toString().toLowerCase() + MainColor + "!");
        }
        else {
            String c = changer.getName().equalsIgnoreCase("CONSOLE") ? "konsole" : changer.getName();
            Util.sendMsg(p, MainColor + "Twoj tryb gry zostal zmieniony na " + ImportantColor + mode.toString().toLowerCase() + MainColor + " przez " + ImportantColor + c + MainColor + "!");
            Util.sendMsg(changer, MainColor + "Tryb gry gracza " + ImportantColor + p.getName() + MainColor + " zostal zmieniony na " + ImportantColor + mode.toString().toLowerCase() + MainColor + "!");
        }
    }
    
    public boolean onExecute(CommandSender sender, String[] args) {
        String[] survival = { "0", "s", "survival" };
        String[] creative = { "1", "c", "creative" };
        String[] adventure = { "2", "a", "adventure" };
        String [] spectator = { "3" ,"s" ,"spectator"};
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (args.length == 1) {
                String mode = args[0];
                if (Util.containsIgnoreCase(survival, mode)) {
                    setGamemode(p, GameMode.SURVIVAL, null);
                }
                else if (Util.containsIgnoreCase(creative, mode)) {
                    setGamemode(p, GameMode.CREATIVE, null);
                }
                else if (Util.containsIgnoreCase(adventure, mode)) {
                    setGamemode(p, GameMode.ADVENTURE, null);
                }
                else if (Util.containsIgnoreCase(spectator, mode)){
                	setGamemode(p, GameMode.SPECTATOR, null);
                }
                else {
                    Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Nie poprawny tryb gamemode");
                }
            }
            else {
                if (args.length != 2) {
                    return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
                }
                if (!sender.hasPermission("core.gamemode.others")) {
                    return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ":" + WarningColor_2 + " Nie masz praw do tej komendy! " + SpecialSigns + "(" + WarningColor + "core.gamemode.others" +  SpecialSigns + ")");
                }
                Player op = Bukkit.getPlayer(args[0]);
                if (op == null) {
                    return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
                }
                String mode2 = args[1];
                if (Util.containsIgnoreCase(survival, mode2)) {
                    setGamemode(op, GameMode.SURVIVAL, p);
                }
                else if (Util.containsIgnoreCase(creative, mode2)) {
                    setGamemode(op, GameMode.CREATIVE, p);
                }
                else if (Util.containsIgnoreCase(adventure, mode2)) {
                    setGamemode(op, GameMode.ADVENTURE, p);
                }
                else if (Util.containsIgnoreCase(spectator, mode2)){
                	setGamemode(op, GameMode.SPECTATOR, p);
                }
                else {
                    Util.sendMsg(sender, WarningColor + "Blad " +  SpecialSigns + ": " + WarningColor_2 + "Nie poprawny tryb gamemode");
                }
            }
            return true;
        }
        if (args.length != 2) {
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        Player op2 = Bukkit.getPlayer(args[0]);
        if (op2 == null) {
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        }
        String mode = args[1];
        if (Util.containsIgnoreCase(survival, mode)) {
            setGamemode(op2, GameMode.SURVIVAL, sender);
        }
        else if (Util.containsIgnoreCase(creative, mode)) {
            setGamemode(op2, GameMode.CREATIVE, sender);
        }
        else if (Util.containsIgnoreCase(adventure, mode)) {
            setGamemode(op2, GameMode.ADVENTURE, sender);
        }
        else if (Util.containsIgnoreCase(spectator, mode)){
        	setGamemode(op2, GameMode.SPECTATOR, sender);
        }
        else {
            Util.sendMsg(sender, WarningColor + "Blad " +  SpecialSigns + ": " + WarningColor_2 + "Nie poprawny tryb gamemode");
        }
        return true;
    }
}
