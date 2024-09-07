package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.core.utils.PlayerInventoryUtil;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.user.ClearInventoryPacket;
import pl.justsectors.redis.client.RedisClient;

public class ClearCommand extends PlayerCommand implements Colors
{
    public ClearCommand() {
        super("clearinv", "Czyszczenie ekwipunku graczy", "/clearinv [gracz]", "core.clearinv", "clear", "clearinventory", "ci");
    }
    
    public boolean onCommand(Player sender, String[] args) {
        if (args.length != 1) {
        	PlayerInventoryUtil.ClearPlayerInventory(sender);
            return Util.sendMsg(sender, MainColor + "Twoj ekwipunek zostal " + ImportantColor + "wyczyszczony" + MainColor + "!");
        }
        if (!sender.hasPermission("core.clearinv.others")) {
            return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Nie posiadasz uprawnien do tej komendy " + SpecialSigns + "(" +  WarningColor + "core.clearinv.others" + SpecialSigns + ")");
        }
        final User user = UserManager.getUser(args[0]);
        if (user == null) {
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        }
        if(!user.isOnline()){
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        }
        final ClearInventoryPacket packet = new ClearInventoryPacket(user.getLastName(), sender.getDisplayName());
        RedisClient.sendSectorsPacket(packet);
        return Util.sendMsg(sender, MainColor + "Wyczyszczono ekwipunek gracza " + ImportantColor + user.getLastName() + MainColor + "&7!");
    }
}
