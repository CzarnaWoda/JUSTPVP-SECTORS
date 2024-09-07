package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.inventories.TopInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class TopCommand extends PlayerCommand{

	public TopCommand() {
		super("top", "top10 graczy", "/top", "core.top", "topka", "top10");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		TopInventory.openMenu(p);
		return false;
	}

}
