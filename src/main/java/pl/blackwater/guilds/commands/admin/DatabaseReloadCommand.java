package pl.blackwater.guilds.commands.admin;

import org.bukkit.entity.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwater.guilds.ranking.RankingManager;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;

public class DatabaseReloadCommand extends PlayerCommand {

    public DatabaseReloadCommand() {
        super("reloadguild", "", "/ga reloadguild", "guild.admin");
    }

    @Override
    public boolean onCommand(Player p, String[] args) {
        RankingManager.getGuildRankings().clear();
        GuildManager.resetup();
        p.sendMessage(Util.fixColor(Util.replaceString("&8>> &cPrzeladowano gildie na serwerze ;)")));
        return false;
    }
}
