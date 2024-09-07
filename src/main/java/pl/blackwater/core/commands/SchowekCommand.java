package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.inventories.SchowekInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class SchowekCommand extends PlayerCommand{

	public SchowekCommand() {
		super("schowek", "otwiera schowek menu", "/schowek", "core.schowek", "depozyt");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		SchowekInventory.openMenu(p);
		return false;
	}

}
