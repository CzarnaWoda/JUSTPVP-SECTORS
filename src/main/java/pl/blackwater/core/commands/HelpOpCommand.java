package pl.blackwater.core.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.others.HelpOpMessagePacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.HashMap;
import java.util.UUID;

public class HelpOpCommand extends PlayerCommand implements Colors
{
    private static HashMap<UUID, Long> times;
    
    static {
        times = new HashMap<>();
    }
    
    public HelpOpCommand() {
        super("helpop", "helpop", "/helpop <wiadomosc>", "core.helpop");
    }
    
    public boolean onCommand(Player sender, String[] args) {
        if (args.length < 1) {
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        final Long t = HelpOpCommand.times.get(sender.getUniqueId());
        if (t != null && System.currentTimeMillis() - t < 30000L) {
            return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Na HelpOP mozesz pisac co 30 sekund!");
        }
        final String message = ChatColor.stripColor(Util.fixColor(StringUtils.join(args, " ")));
        final HelpOpMessagePacket packet = new HelpOpMessagePacket(message,sender.getDisplayName());
        RedisClient.sendSectorsPacket(packet);
        HelpOpCommand.times.put(sender.getUniqueId(), System.currentTimeMillis());
        return Util.sendMsg(sender, SpecialSigns + "[" + ImportantColor + BOLD + UnderLined + "HelpOP" + SpecialSigns + "]" + ImportantColor + sender.getName() + MainColor + " -> " + ImportantColor + message);
    }
    
    public static HashMap<UUID, Long> getTimes() {
        return HelpOpCommand.times;
    }
}
