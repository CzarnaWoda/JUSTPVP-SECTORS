package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.inventories.KitsInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class KitCommand extends PlayerCommand
{

	public KitCommand()
	{
		super("kit", "otwiera kit menu", "/kit", "core.kit", "zestaw","kits","zestawy","kity");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		KitsInventory.openKitMenu(p);
		return false;
	}

}
