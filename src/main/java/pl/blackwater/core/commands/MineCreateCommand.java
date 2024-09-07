package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.MineManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.sectors.SectorType;

public class MineCreateCommand extends PlayerCommand{

	public MineCreateCommand() {
		super("minecreate", "kopalnia manager", "&4Blad: &6Poprawne uzycie: /minecreate <nazwa> <type[NORMAL/GUILD]> <koszt> <level> <mainregion> <mineregion> <stonelimit>", "core.maincreate");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
			if(args.length == 7){
					String name;
					String type;
					int cost;
					int level;
					String mainregion;
					String mineregion;
					int stonelimit;
					if(args[0] != null)
						name = args[0];
					else
						return Util.sendMsg(p, getUsage());
					if(args[1] != null)
						type = args[1];
					else
						return Util.sendMsg(p, getUsage());
					if(!Util.isInteger(args[2]))
						return Util.sendMsg(p, getUsage());
					else
						cost = Integer.parseInt(args[2]);
					if(!Util.isInteger(args[3]))
						return Util.sendMsg(p, getUsage());
					else
						level = Integer.parseInt(args[3]);
					if(args[4] != null)
						mainregion = args[4];
					else
						return Util.sendMsg(p, getUsage());
					if(args[5] != null)
						mineregion = args[5];
					else
						return Util.sendMsg(p, getUsage());
					if(!Util.isInteger(args[6]))
						return Util.sendMsg(p, getUsage());
					else
						stonelimit = Integer.parseInt(args[6]);
					final User u = UserManager.getUser(p);
					if(u.getSector().getSectorType() != SectorType.MINE)
					{
						return Util.sendMsg(p, "&4Blad! &cNie mozesz utworzyc kopalni na tym sektorze!");
					}
					MineManager.CreateMine(name,type, cost, p.getLocation(), level, mainregion,mineregion,stonelimit);
					Util.sendMsg(p, "&7Stworzono &6kopalnie! &7Jej parametry: nazwa: &6" + name + ", &7typ kopalni: &6" + type + ", &7koszt: &6" + cost + ", &7level aby mozna bylo zakupic: &6" + level + ", &7nazwa regionu glownego: &6" + mainregion + ", &7nazwa regionu kopalnianego: &6" + mineregion + ", &7dzienny limit kopania w koplani: &6" + stonelimit);
				}else
					return Util.sendMsg(p, getUsage());
		return false;
	}

}
