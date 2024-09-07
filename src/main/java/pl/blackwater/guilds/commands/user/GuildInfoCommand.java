package pl.blackwater.guilds.commands.user;

import org.bukkit.entity.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.settings.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.inventories.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;

public class GuildInfoCommand extends PlayerCommand implements Colors {

    public GuildInfoCommand() {
        super("info", "Informacje o danej gildii", "/g info <gildia>", "guild.gracz", "informacje");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}" , getUsage()));
        }
        Guild g = GuildManager.getGuild(args[0]);
        if(g == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Taka gildia nie istnieje!");
        }
        GuildInfoInventory.openMenu(player,g);
        return false;
    }
}
