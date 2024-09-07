package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.inventories.ServerInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class ServerCommand extends PlayerCommand {
    public ServerCommand() {
        super("server", "Informacje o serwerze!", "/server", "core.cmd.server", "serwer");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        ServerInventory.ServerInventory(player);
        return true;
    }
}
