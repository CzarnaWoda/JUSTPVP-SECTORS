package pl.blackwaterapi.timer;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.blackwaterapi.utils.Util;

public class TimerUtil
{
    public static void teleport(Player p, Location location, int delay) {
        if (!p.hasPermission("api.timer.bypass")) {
            Util.sendMsg(p, Util.fixColor(Util.replaceString("&8->> &7Teleport nastapi za &a" + Util.secondsToString(delay) + "&7!")));
        }
        TimerManager.addTask(p, new TimerCallback<Player>() {
            @SuppressWarnings("deprecation")
			@Override
            public void success(Player player) {
                player.teleport(location);
                Util.sendMsg(player, Util.replaceString("&8->> &2%V% &aPrzeteleportowano"));
            	Location loc = player.getLocation();
            	player.getWorld().refreshChunk(loc.getBlockX(), loc.getBlockZ());
            }
            
            @Override
            public void error(Player player) {
                Util.sendMsg(player, "&4Blad: &cTeleport zostal przerwany!");
            }
        }, delay);
    }
}
