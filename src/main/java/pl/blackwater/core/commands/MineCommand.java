package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.managers.MineManager;
import pl.blackwaterapi.commands.PlayerCommand;

public class MineCommand extends PlayerCommand{

	public MineCommand() {
		super("mine", "mine menu", "/mine", "core.mine", "kopalnia");
	}

	@Override
	public boolean onCommand(Player p, String[] arg1) {
		MineManager.OpenMineMenu(p);
		return false;
	}

}
