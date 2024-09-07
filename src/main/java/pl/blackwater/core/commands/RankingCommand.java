package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.inventories.RankingInventory;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class RankingCommand extends PlayerCommand
{

	public RankingCommand()
	{
		super("ranking", "pokazuje statystyki gracza", "/ranking <nickname>", "core.ranking", "stats","statystyki","graczinfo");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length == 0){
			RankingInventory.openMenu(p, UserManager.getUser(p));
		}else
		{
			User o = UserManager.getUser(args[0]);
			if(o == null) 
				return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
			RankingInventory.openMenu(p, o);
		}
		return false;
	}

}
