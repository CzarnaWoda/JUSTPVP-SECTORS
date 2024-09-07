package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class HealCommand extends PlayerCommand implements Colors
{
    public HealCommand() {
        super("heal", "Uleczanie graczy", "/heal [gracz]", "core.heal");
    }
    
    public boolean onCommand(Player sender, String[] args) {
        if (args.length != 1) {
            sender.setHealth(20.0);
            sender.setFoodLevel(20);
            sender.setFireTicks(0);
            for (PotionEffect potionEffect : sender.getActivePotionEffects()) {
                sender.removePotionEffect(potionEffect.getType());
            }
            return Util.sendMsg(sender, MainColor + "Zostales uleczony!");
        }
        if (!sender.hasPermission("core.heal.others")) {
            return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " +  WarningColor_2 + "Nie masz praw do tej komendy! " + SpecialSigns + "(" + WarningColor + "core.heal.others" + SpecialSigns + ")");
        }
        Player o = Bukkit.getPlayer(args[0]);
        if (o == null) {
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        }
        o.setHealth(20);
        o.setFoodLevel(20);
        o.setFireTicks(0);
        for (PotionEffect potionEffect2 : o.getActivePotionEffects()) {
            o.removePotionEffect(potionEffect2.getType());
        }
        Util.sendMsg(o, MainColor + "Zostales uleczony przez " + ImportantColor + sender.getName() + MainColor + "!");
        return Util.sendMsg(sender, MainColor + "Uleczyles gracza " + ImportantColor + o.getName() + MainColor + "!");
    }
}
