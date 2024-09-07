package pl.blackwater.guilds.commands.user;

import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.War;
import pl.blackwater.guilds.inventories.WarInfoInventory;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.WarManager;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class WarInfoCommand extends PlayerCommand implements Colors {
    public WarInfoCommand() {
        super("sprawdzwojne", "Sprawdza informacje na temat danej wojny", "/g sprawdzwojne <gildia1> <gildia2>", "guild.gracz", "wojnainfo","warinfo","checkwar");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(!GuildConfig.ENABLEMANAGER_WAR)
            return Util.sendMsg(player,WarningColor + "Blad: "  + WarningColor_2 + "Wojny zostaly globalnie wylaczone przez administratora");
        if(args.length != 2){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        final Guild g = GuildManager.getGuild(args[0]);
        if(g == null){
            return Util.sendMsg(player, WarningColor + "Blad: "+ WarningColor_2 + "Gildia o tagu " + args[0] + " nie istnieje");
        }
        final Guild o = GuildManager.getGuild(args[1]);
        if(o == null){
            return Util.sendMsg(player, WarningColor + "Blad: "+ WarningColor_2 + "Gildia o tagu " + args[1] + " nie istnieje");
        }
        final War w = WarManager.getWarWithGuild(g,o);
        if(w == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Te gildie nie mają ze sobą wojny");
        }
        WarInfoInventory.openMenu(player, w);
        return false;
    }
}
