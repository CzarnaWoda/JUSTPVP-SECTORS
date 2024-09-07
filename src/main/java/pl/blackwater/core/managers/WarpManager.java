package pl.blackwater.core.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import org.bukkit.event.player.PlayerTeleportEvent;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Warp;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.timer.TimerUtil;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.redis.channels.RedisChannel;

public class WarpManager implements Colors
{
public static final ConcurrentHashMap<String, Warp> warps = new ConcurrentHashMap<>();
  
  public static Warp getWarp(final String name)
  {
    for (Warp w : warps.values()) {
      if (name.equalsIgnoreCase(w.getName())) {
        return w;
      }
    }
    return null;
  }
  
  public static Warp addWarp(final String name,final Location location,final String pex)
  {
	final Warp w = new Warp(name, location, pex);
    warps.put(name, w);
    return w;
  }

  public static void registerWarp(final Warp warp)
  {
    warps.put(warp.getName(), warp);
  }

  public static void deleteWarp(final String name)
  {
    warps.remove(name);
  }
  
public static List<String> getWarpByGroup(final Player p)
  {
	final List<String> warp = new ArrayList<>();
    for (Warp w : warps.values()) {
      if (p.hasPermission(w.getPex())) {
        warp.add(w.getName());
      }
    }
    return warp;
  }
public static List<String> getWarpByPex(Player p, String pex)
  {
	final List<String> warp = new ArrayList<>();
    for (Warp w : warps.values()) {
      if (w.getPex().equals(pex)) {
        warp.add(w.getName());
      }
    }
    return warp;
  }
  public static void getRandomWarp(final Player p)
  {
	  final List<String> warps1 = getWarpByPex(p, "core.mapa");
	  final int idx = new Random().nextInt(warps1.size());
	  final String random = warps1.get(idx);
	  Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Wybrano randomowy" + ImportantColor + " warp"));
	  Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Wybrany warp to: " + ImportantColor + UnderLined + random));
	  Bukkit.dispatchCommand(p,"warp " + random);
  }
  //TimerUtil.teleport(p, w.getLocation(), 10);
  public static void getRandomWarpFastTP(final Player p)
  {
	  final List<String> warps1 = getWarpByPex(p, "core.mapa");
	  final int idx = new Random().nextInt(warps1.size());
	  final String random = warps1.get(idx);
	  final Warp w = getWarp(random);
	  Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Wybrano randomowy" + ImportantColor + " warp"));
	  Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Wybrany warp to: " + ImportantColor + UnderLined + random));
      assert w != null;
      TimerUtil.teleport(p, w.getLocation(), 1);
  }
  public static void getRandomWarpInstant(final Player p)
  {
    final List<String> warps1 = getWarpByPex(p, "core.mapa");
    final int idx = new Random().nextInt(warps1.size());
    final String random = warps1.get(idx);
    final Warp w = getWarp(random);
    Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Wybrano randomowy" + ImportantColor + " warp"));
    Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Wybrany warp to: " + ImportantColor + UnderLined + random));
    assert w != null;
    p.teleport(w.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
  }
  public static void setup()
  {
    RedisChannel.INSTANCE.WARPS.forEach((name, s) -> {
      Warp warp = GsonUtil.fromJson(s, Warp.class);
      warps.put(name, warp);
    });
    Logger.info("Loaded " + warps.size() + " warps");
  }
  
  public static ConcurrentHashMap<String, Warp> getWarps()
  {
    return warps;
  }
}
