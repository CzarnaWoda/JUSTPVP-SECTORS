package pl.blackwater.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.user.ProtectionDisablePacket;
import pl.justsectors.redis.client.RedisClient;


public class ProtectionCommand extends PlayerCommand implements Colors {
    public ProtectionCommand() {
        super("ochrona", "Pokazuje ile zostalo czasu ochrony", "/ochrona", "core.protection", "protection");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        final User user = UserManager.getUser(player);
        if(args.length != 1) {
            Util.sendMsg(player, Util.replaceString(SpecialSigns + ">> " + MainColor + "Status twojej ochrony: " + (user.isProtection() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")));
            if (user.isProtection()) {
                Util.sendMsg(player, Util.replaceString(SpecialSigns + ">> " + MainColor + "Ochrona potrwa jescze: " + ImportantColor + Util.secondsToString((int) ((user.getProtection() - System.currentTimeMillis()) / 1000L))));
            }
            Util.sendMsg(player, Util.replaceString(SpecialSigns + ">>" + MainColor + " Chcesz wylaczyc ochrone? Wpisz &a&n/ochrona off"));
            return false;
        }else{
            if(args[0].equalsIgnoreCase("off")){
                if(user.isProtection()){
                    final ProtectionDisablePacket packet = new ProtectionDisablePacket(user.getUuid());
                    RedisClient.sendSectorsPacket(packet);
                    return Util.sendMsg(player, "&aGratulacje&7, wylaczyles swoja ochrone!");
                }
            }else{
                Util.sendMsg(player, Util.replaceString(SpecialSigns + ">> " + MainColor + "Status twojej ochrony: " + (user.isProtection() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%")));
                if (user.isProtection()) {
                    Util.sendMsg(player, Util.replaceString(SpecialSigns + ">> " + MainColor + "Ochrona potrwa jescze: " + ImportantColor + Util.secondsToString((int) ((user.getProtection() - System.currentTimeMillis()) / 1000L))));
                }
                Util.sendMsg(player, Util.replaceString(SpecialSigns + ">>" + MainColor + " Chcesz wylaczyc ochrone? Wpisz &a&n/ochrona off"));
                return false;
            }
        }
        return true;
    }
}
