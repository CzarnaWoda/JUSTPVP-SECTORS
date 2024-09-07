package pl.blackwater.core.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import pl.blackwater.core.data.Warp;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.WarpManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.timer.TimerUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;

public class WarpCommand extends PlayerCommand implements Colors{

	public WarpCommand() {
		super("warp", "teleportacja na wybrany warp", "/warp", "core.warp");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE) || SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.MINE)){
			return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz korzystac z tej komendy na tym sektorze!");
		}
	    if (args.length < 1)
	    {
	      if (WarpManager.getWarpByGroup(p).size() == 0) {
	        return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie ma zadnych warpow!");
	      }
	      Util.sendMsg(p, SpecialSigns + "* " + MainColor + "Warpy" + SpecialSigns + " (" + ImportantColor + "Poza Mapa" + SpecialSigns + "): " + MainColor + "" + StringUtils.join(WarpManager.getWarpByPex(p, "core.pozamapa"),SpecialSigns + ", " + MainColor));
	      Util.sendMsg(p, SpecialSigns + "* " + MainColor + "Warpy" + SpecialSigns + " (" + ImportantColor + "Na Mapie" + SpecialSigns + "): " + MainColor + "" + StringUtils.join(WarpManager.getWarpByPex(p,"core.mapa"), SpecialSigns + ", " + MainColor));
	      Util.sendMsg(p, SpecialSigns + "* " + MainColor + "Warpy" + SpecialSigns + " (" + ImportantColor + "Wszystkie" + SpecialSigns + "): " + MainColor + "" + StringUtils.join(WarpManager.getWarpByGroup(p), SpecialSigns + ", " + MainColor));
	      return true;
	    }
	    if (args[0].equalsIgnoreCase("random")){
	    	WarpManager.getRandomWarp(p);
	    	return true;
	    }
	    final Warp w = WarpManager.getWarp(args[0]);
	    if (w == null) {
	      return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Warp nie istnieje!");
	    }
	    if (!p.hasPermission(w.getPex())) {
	      return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie masz dostepu do tego warpu!");
	    }
	    TimerUtil.teleport(p, w.getLocation(), 10);
	    return true;
	}

}
