package pl.blackwater.core.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.inventories.ChannelInventory;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.timer.TimerUtil;
import pl.blackwaterapi.utils.RandomUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;

public class SpawnCommand extends PlayerCommand implements Colors{

	public SpawnCommand() {
		super("spawn", "teleportuje do spawn'a", "/spawn [numer]", "core.spawn");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(CoreConfig.SPAWNMANAGER_SPAWNLIST.isEmpty())
			return Util.sendMsg(p, Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Nie ustawiono jeszcze lokacji spawn'u na serwerze!"));
		final Optional<Sector> sector = SectorManager.getCurrentSector();
		if(sector.isPresent() && sector.get().getSectorType() == SectorType.MINE)
		{
			ChannelInventory.openInventory(p);
			return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz isc na spawn na tym sektorze!");
		}
		if(sector.isPresent() && sector.get().getSectorType() == SectorType.STORAGE){
			ChannelInventory.openInventory(p);
			return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz isc na spawn na tym sektorze!");
		}
		final List<Location> locationList = new ArrayList<>();
		for(String s : CoreConfig.SPAWNMANAGER_SPAWNLIST)
			locationList.add(LocationUtil.convertStringToLocation(s));
		if(args.length == 0) {
			Location loc;
			if(locationList.size() > 1) {
				loc = locationList.get(RandomUtil.getRandInt(0, locationList.size() - 1));
			}else
				loc = locationList.get(0);
			TimerUtil.teleport(p, loc, 10);
		}else {
			if(!Util.isInteger(args[0]))
				return Util.sendMsg(p, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "To nie jest liczba !")));
			final int i = Integer.parseInt(args[0]);
			if(locationList.size() < i || i == 0)
				return Util.sendMsg(p, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Taki spawn nie istnieje !")));
			final Location loc = locationList.get(i - 1);
			TimerUtil.teleport(p, loc, 10);
		}
		return false;
	}

}
