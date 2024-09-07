package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class ACommand extends PlayerCommand implements Colors
{
    public ACommand() {
        super("a", "komenda a", "/a <wiadomosc>", "core.a");
    }
    
    public boolean onCommand(Player player, String[] args) {
        if (args.length > 0) {
            String message = Util.fixColor(MessageConfig.MESSAGE_ADMINCHAT);
            if (player != null) {
            	message = message.replaceAll("%name", player.getDisplayName());
            }
            else {
                assert false;
                message = message.replaceAll("%name", player.getName().toLowerCase());
                
            }
            StringBuilder msg = new StringBuilder();
            msg.append(args[0]);
            for (int i = 1; i < args.length; ++i) {
                msg.append(" ").append(args[i]);
            }
            message = message.replaceAll("%message", msg.toString());
            message = ChatColor.translateAlternateColorCodes('&', message);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("core.adminchat.show")) {
                    p.sendMessage(message);
                }
            }
            Bukkit.getConsoleSender().sendMessage(message);
            return false;
        }else{
        	player.sendMessage(MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        return false;
    }
}
