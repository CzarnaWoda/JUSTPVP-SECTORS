package pl.blackwater.market.commands;

import org.bukkit.entity.Player;

import pl.blackwater.market.utils.MarketUtil;
import pl.blackwaterapi.commands.PlayerCommand;

public class MarketCommand extends PlayerCommand{

	public MarketCommand() {
		super("market", "market command", "/market", "market.market", new String[0]);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		MarketUtil.openMainMenu(p);
		return false;
	}

}
