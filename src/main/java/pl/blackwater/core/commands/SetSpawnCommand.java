package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.blackwater.core.inventories.SetSpawnInventory;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.commands.PlayerCommand;

public class SetSpawnCommand extends PlayerCommand{

	public SetSpawnCommand() {
		super("setspawn", "teleportuje na spawn", "/setspawn", "core.setspawn");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length == 1) {
			p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			p.getWorld().save();
		}
		SetSpawnInventory.openMenu(p);
		if(!CoreConfig.SPAWNMANAGER_SPAWNLIST.isEmpty()) {
			Bukkit.getWorld("world").setSpawnLocation(CoreConfig.getSpawnLocations().get(0).getBlockX(), CoreConfig.getSpawnLocations().get(0).getBlockY(), CoreConfig.getSpawnLocations().get(0).getBlockZ());
		}
		return false;
	}

}
