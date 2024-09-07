package pl.blackwater.guilds.listeners;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwaterapi.utils.*;

public class EntityDamageByEntityListener implements Listener{


    @EventHandler(priority=EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.isCancelled()) {
            return;
        }
        if (event.getDamage() <= 0.0D) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (Util.getDamager(event) == null) {
            return;
        }
        Player p = (Player)event.getEntity();
        Player d = Util.getDamager(event);
        if (p == d) {
            return;
        }
        Guild pg = GuildManager.getGuild(p);
        Guild dg = GuildManager.getGuild(d);
        if ((dg == null) || (pg == null)) {
            return;
        }
        if (dg != pg)
        {
            final Alliance alliance = AllianceManager.getAlliance(pg,dg);
            if (alliance != null) {
                if(alliance.isPvp()) {
                    event.setDamage(0.0D);
                }else{
                    event.setCancelled(true);
                }
            }
            return;
        }
        if (pg.isPvp())
        {
            event.setDamage(0.0D);
        }else {
            event.setCancelled(true);
        }
    }
}
