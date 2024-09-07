package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.inventories.ShopsInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class ShopCommand extends PlayerCommand
{

	public ShopCommand()
	{
		super("shop", "open shopinventory", "/shop", "core.shop", "sklep","sklepik","sell","buy");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		ShopsInventory.openMainMenu(p);
		return false;
	}

}
