package pl.blackwaterapi.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import pl.blackwater.core.Core;
import pl.blackwaterapi.utils.TimeUtil;

public class TimerManager implements Listener
{
    private static Map<UUID, TimerTask> tasks;
    
    public static void addTask(Player player, TimerCallback<Player> call, int time) {
        if (player.hasPermission("api.timer.bypass")) {
            call.success(player);
            return;
        }
        TimerTask t = TimerManager.tasks.get(player.getUniqueId());
        if (t != null) {
            t.cancel(player);
            return;
        }
        t = new TimerTask(player.getUniqueId(), call);
        TimerManager.tasks.put(player.getUniqueId(), t);
        t.runTaskLater((Plugin) Core.getPlugin(), (long)TimeUtil.SECOND.getTick(time));
    }
    
    private static void cancel(TimerTask task, Player player) {
        task.cancel(player);
        TimerManager.tasks.remove(player.getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        TimerTask t = TimerManager.tasks.get(event.getPlayer().getUniqueId());
        if (t != null) {
            cancel(t, event.getPlayer());
            return;
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            TimerTask t = TimerManager.tasks.get(player.getUniqueId());
            if (t != null) {
                cancel(t, player);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TimerTask t = TimerManager.tasks.get(player.getUniqueId());
        if (t != null) {
            cancel(t, player);
        }
    }
    
    static {
        tasks = new HashMap<UUID, TimerTask>();
    }
    
    public static class TimerTask extends BukkitRunnable
    {
        private UUID player;
        private TimerCallback<Player> call;
        
        public void run() {
            this.call.success(Bukkit.getPlayer(this.player));
            TimerManager.tasks.remove(this.player);
        }
        
        public void cancel(Player player) {
            super.cancel();
            this.call.error(player);
        }
        
        public TimerTask(UUID player, TimerCallback<Player> call) {
            super();
            this.player = player;
            this.call = call;
        }
        
        public UUID getPlayer() {
            return this.player;
        }
    }
}
