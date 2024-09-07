package pl.blackwater.guilds.commands.user;

import org.bukkit.entity.*;
import pl.blackwater.core.data.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.managers.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;

public class MineCommand extends PlayerCommand implements Colors {
    public MineCommand() {
        super("mine", "kopalnia gildii", "/g mine", "guild.gracz");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii");
        }
        final Mine m = MineManager.getMineByGuild(g);
        if (m == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Twoja gildia nie posiada kopalni");
        }
        if(!m.getType().equalsIgnoreCase("GUILD")){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "ERROR_1 (pl.blackwater.data.Mine has bad TYPE and we can't get method to open inventory!)");
        }
        MineManager.OpenMineMenuWhenPlayerBought(player,m);
        return false;
    }
}
