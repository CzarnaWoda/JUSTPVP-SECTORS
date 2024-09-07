package pl.blackwater.core.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.chat.TellMessagePacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.UUID;

public class ReplyCommand extends PlayerCommand implements Colors
{
    public ReplyCommand() {
        super("reply", "odpowiedz na prywatna wiadomosc", "/reply <wiadomosc>", "core.reply", "r");
    }
    
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 1) {
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        final UUID last = TellCommand.getLastMsg().get(player.getUniqueId());
        if (last == null) {
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie masz komu odpisac!");
        }
        final User other = UserManager.getUser(last);
        if (other == null || !other.isOnline()) {
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        }
        final String message = ChatColor.stripColor(Util.fixColor(StringUtils.join(args, " ", 0, args.length)));
        final RedisPacket packet = new TellMessagePacket(player.getUniqueId(), player.getDisplayName(), other.getUuid(), other.getLastName(), message);
        RedisClient.sendSectorsPacket(packet);
        return true;
        }
    }
