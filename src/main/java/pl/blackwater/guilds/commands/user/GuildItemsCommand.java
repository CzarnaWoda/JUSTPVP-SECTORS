package pl.blackwater.guilds.commands.user;

import org.bukkit.entity.*;
import pl.blackwater.guilds.inventories.*;
import pl.blackwaterapi.commands.*;

public class GuildItemsCommand extends PlayerCommand {
    public GuildItemsCommand() {
        super("itemy", "Pokazuje przedmioty wymagne do zalozenia gildii", "/g itemy", "guild.gracz");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        GuildItemsInventory.openMenu(player);
        return true;
    }
}
