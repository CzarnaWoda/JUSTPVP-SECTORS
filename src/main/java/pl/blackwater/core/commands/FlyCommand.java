package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.user.FlyTogglePacket;
import pl.justsectors.redis.client.RedisClient;

public class FlyCommand extends PlayerCommand implements Colors
{
    public FlyCommand() {
        super("fly", "zarzadzanie trybem latania graczy", "/fly [gracz]", "core.fly");
    }
    
    public boolean onCommand(Player p, String[] args) {
        if (args.length == 1) {
            if (!p.hasPermission("core.fly.others")) {
                return Util.sendMsg(p, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Nie posiadasz uprawnien do tej komendy " + SpecialSigns + "(" + WarningColor + "core.fly.others" + SpecialSigns + ")");
            }
            User u = UserManager.getUser(args[0]);
            if (u == null) {
                return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
            }
            if(u.isOnline()) {
                final FlyTogglePacket  packet = new FlyTogglePacket(u.getLastName(),p.getDisplayName());
                RedisClient.sendSectorsPacket(packet);
                Util.sendMsg(p, ImportantColor + "" + (u.isFly() ? "Wlaczono" : "Wylaczono") + MainColor + " tryb latania dla gracza " + ImportantColor + u.getLastName() + MainColor + "!");
                return true;
            }else{
                return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
            }
        }
        else {
            User u2 = UserManager.getUser(p);
            if (u2 == null) {
                return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
            }
            final FlyTogglePacket packet = new FlyTogglePacket(u2.getLastName(),p.getDisplayName());
            RedisClient.sendSectorsPacket(packet);
            return Util.sendMsg(p, ImportantColor + "" + (u2.isFly() ? "Wlaczono" : "Wylaczono") + MainColor + " tryb latania!");
        }
    }
}
