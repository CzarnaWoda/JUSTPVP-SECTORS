package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.inventories.OsiagnieciaInventory;
import pl.blackwaterapi.commands.PlayerCommand;

public class OsiagnieciaCommand extends PlayerCommand{

	public OsiagnieciaCommand() {
		super("osiagniecia", "questy", "/osiagniecia", "core.osiagniecia", "os","questy","quest","osiag","quests","zadania","achivements","achiv","zad");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		OsiagnieciaInventory.openMenu(p);
		return false;
	}

}
