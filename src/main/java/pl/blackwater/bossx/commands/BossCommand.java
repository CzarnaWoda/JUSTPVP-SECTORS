package pl.blackwater.bossx.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import pl.blackwater.bossx.base.BossManager;
import pl.blackwater.bossx.settings.BossConfig;
import pl.blackwater.bossx.settings.RewardsConfig;
import pl.blackwater.bossx.utils.GUI;
import pl.blackwater.core.Core;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class BossCommand extends PlayerCommand{

	public BossCommand() {
		super("boss", "/boss spawn/remove", "/boss [spawn/remove/reload/rewards]", "bossx.cmd.boss", new String[0]);
	}

	@Override
	public boolean onCommand(final Player p, String[] args) {
		if(args.length != 1){
			return Util.sendMsg(p, "&4Blad: &cPoprawne uzycie:&c " + getUsage());
		}
        if (args[0].equalsIgnoreCase("spawn")){
        final Location loc = new Location(Bukkit.getWorld("world"), p.getLocation().getX(), 80.0, p.getLocation().getZ());
        loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
        Bukkit.getServer().getScheduler().runTaskLater(Core.getPlugin(), (Runnable)new Runnable() {
            public void run() {
            	BossManager.SpawnBoss(p.getWorld(), loc, BossConfig.bossname, BossConfig.bosshealth);
            }
        }, 10L);
        }else if (args[0].equalsIgnoreCase("remove")){
        	for(Entity e : Bukkit.getServer().getWorld("world").getEntities()) {
        		if (e.getType() == EntityType.GIANT){
                		e.remove();
            }
    	}
    	Util.sendMsg(p, BossConfig.messageprefix + " &6Zabito wszystkie &cbossy &6na mapie !");
		return false;
	}else if (args[0].equalsIgnoreCase("reload")){
        	//TODO
		Util.sendMsg(p, BossConfig.reloadmessage);
	}else if (args[0].equalsIgnoreCase("rewards")){
		GUI.OpenMenu(p);
	}else{
		return Util.sendMsg(p, "&4Blad: &cPoprawne uzycie:&c " + getUsage());
	}
		return false;
	}

}
