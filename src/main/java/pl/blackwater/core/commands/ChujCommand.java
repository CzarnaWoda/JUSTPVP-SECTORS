package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.guilds.scoreboard.ScoreBoardNameTag;
import pl.blackwaterapi.commands.PlayerCommand;

public class ChujCommand extends PlayerCommand {
    public ChujCommand() {
        super("niewiem", "nie", "wiem", "core.niewiem", "niemampojecia");
    }

    @Override
    public boolean onCommand(Player p, String[] p1) {

        ScoreBoardNameTag.scoreboards = !ScoreBoardNameTag.scoreboards;

        p.sendMessage("Mordo, zmieniles jakies gowno na " + (ScoreBoardNameTag.scoreboards ? "Wieksze gowno (TRUE)" : "Mniejsze gowno (FALSE)"));
        return true;
    }
}
