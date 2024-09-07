package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

public class HeadCommand extends PlayerCommand implements Colors
{
    public HeadCommand() {
        super("head", "pobierania glowy gracza", "/head [gracz]", "core.head");
    }
    
    public boolean onCommand(Player player, String[] args) {
        String owner = player.getName();
        if (args.length == 1) {
            owner = args[0];
        }
        player.getInventory().addItem(ItemUtil.getPlayerHead(owner));
        return Util.sendMsg(player, MainColor + "Glowa gracza " + ImportantColor + owner + MainColor + " zostala dodana do twojego ekwipunku!");
    }
}
