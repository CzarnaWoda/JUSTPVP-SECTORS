package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.inventories.ChannelInventory;
import pl.blackwater.core.managers.CombatManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class ChannelCommand extends PlayerCommand
{

    public ChannelCommand()
    {
        super("ch", "otwiera menu channeli", "/ch", "core.ch", "channel");
    }

    @Override
    public boolean onCommand(Player p, String[] args) {
        ChannelInventory.openInventory(p);
        return true;
    }
}
