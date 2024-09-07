package pl.blackwater.core.commands;

import lombok.Getter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TellCommand extends PlayerCommand implements Colors
{
    @Getter private final static HashMap<UUID, UUID> lastMsg  = new HashMap<>();
    @Getter private final static List<String> wyjebane = new ArrayList<>();
    
    public TellCommand() {
        super("tell", "prywatne wiadomosci do graczy", "/tell <gracz> <wiadomosc>", "core.tell", "whisper", "t", "m", "msg");
    }
    
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 2) {
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        final User other = UserManager.getUser(args[0]);
        if (other == null || !other.isOnline()) {
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        }
        if (other.getUserRank().permissions.contains("core.tell.nomsg") && !player.hasPermission("core.tell.nomsg")) {
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz wysylac wiadomosci do tego gracza!");
        }
        if (wyjebane.contains(other.getLastName()) && !player.hasPermission("core.tell.bypass")){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Ten gracz ignoruje prywatne wiadomosci!");
        }
        final String message = ChatColor.stripColor(Util.fixColor(StringUtils.join(args, " ", 1, args.length)));
        final RedisPacket packet = new TellMessagePacket(player.getUniqueId(), player.getDisplayName(), other.getUuid(), other.getLastName(), message);
        RedisClient.sendSectorsPacket(packet);
        return true;
    }
}
