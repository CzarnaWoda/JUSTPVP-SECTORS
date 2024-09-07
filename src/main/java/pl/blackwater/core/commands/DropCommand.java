package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.inventories.DropInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class DropCommand extends PlayerCommand{

	public DropCommand() {
		super("drop", "otwiera menu drop'ow na serwerze!", "/drop", "core.drop");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		DropInventory.openMenu(p);
		return false;
	}

}
