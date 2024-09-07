package pl.blackwater.storage.commands;

import org.bukkit.entity.Player;

import pl.blackwater.storage.inventories.StorageRentInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class StorageCommand extends PlayerCommand {

	public StorageCommand() {
		super("storage", "otwiera menu magazynow", "/storage", "storage.storage", "magazyn","magazyny","storages");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		StorageRentInventory.openMenu(p);
		return false;
	}

}
