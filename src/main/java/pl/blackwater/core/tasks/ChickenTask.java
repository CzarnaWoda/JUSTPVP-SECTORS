package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.core.Core;

public class ChickenTask extends BukkitRunnable
{
    private Player p;
    private Entity chicken;
    private long time;
    private long timeEnd;
    private double speed;
    private boolean first;
    private boolean effect;

    public ChickenTask(Player p) {
        super();
        this.p = p;
        this.chicken = p.getWorld().spawnEntity(p.getLocation().add(0.0, 1.0, 0.0), EntityType.MINECART);
        this.time = System.currentTimeMillis();
        this.effect = p.hasPermission("core.admin.fun");
        this.speed = (p.hasPermission("core.admin.fun") ? 0.6 : 0.4);
        this.timeEnd = (p.hasPermission("core.admin.fun") ? 60000L : 30000L);
        this.chicken.setPassenger(p);
        this.first = false;
    }

    public void run() {
        if (!this.p.isOnline()) {
            this.chicken.remove();
            this.cancel();
            return;
        }
        if (!this.p.isOp() && System.currentTimeMillis() - this.time > this.timeEnd && this.chicken.isOnGround()) {
            if (this.chicken.getPassenger() != null) {
                this.chicken.setPassenger(null);
                Location loc = this.chicken.getLocation();
                Bukkit.getScheduler().runTaskLater(Core.getPlugin(), new Runnable()
                {
                    Player p = this.p;
                    public void run()
                    {
                        this.p.teleport(loc);
                    }
                }, 1L);
            }
            this.chicken.remove();
            this.cancel();
            return;
        }
        if (this.chicken.getLocation().getY() < 256.0) {
            if (this.chicken.getPassenger() != null) {
                this.chicken.setVelocity(this.p.getEyeLocation().getDirection().multiply(this.speed));
                this.p.getLocation().setY(this.chicken.getLocation().getY());
                this.p.getLocation().setZ(this.chicken.getLocation().getZ());
                this.p.getLocation().setX(this.chicken.getLocation().getX());
                if (!this.first) {
                    this.first = true;
                }
                if (this.effect) {
                    for (Player other : Bukkit.getOnlinePlayers()) {
                        Location spawn = this.p.getLocation().getDirection().multiply(-2).add(this.p.getLocation().toVector()).toLocation(this.p.getWorld());
                        if (other.getLocation().distance(this.p.getLocation()) < 30.0) {
                            other.spigot().playEffect(spawn, Effect.CLOUD, 0, 0, 0.3f, 0.3f, 0.3f, 0.05f, 10, 100);
                        }
                    }
                }
            }
            else if (this.first) {
                this.chicken.remove();
                this.cancel();
            }
        }
    }
}
