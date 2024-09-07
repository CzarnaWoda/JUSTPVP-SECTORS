package pl.blackwater.chestpvpdrop.listeners;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pl.blackwater.chestpvpdrop.managers.DropManager;
import pl.blackwater.core.data.User;
import pl.blackwater.core.events.CustomEventManager;
import pl.blackwater.core.events.EventType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.EventManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.customevents.AddStatCustomEventPacket;
import pl.justsectors.redis.client.RedisClient;

public class DropBlockBreakListener implements Listener, Colors

{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        if (!e.isCancelled() && p.getGameMode().equals(GameMode.SURVIVAL)) {
            User u = UserManager.getUser(p);
            if(!EventManager.isOnEventInfinityStone() && u.getBreakStoneDay() >= u.getLimitStone()){
                Util.sendMsg(u.getPlayer(), Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Twój limit stone wyczerpał się " + SpecialSigns + "(" + WarningColor + u.getBreakStoneDay() + SpecialSigns + "/" + WarningColor + u.getLimitStone() + SpecialSigns + ")"));
                e.setCancelled(true);
                return;
            }
            u.addBreakStoneDay(1);
            if(CustomEventManager.getActiveEvent() != null && CustomEventManager.getActiveEvent().getEventType().equals(EventType.BREAK_STONE)){
                final AddStatCustomEventPacket packet = new AddStatCustomEventPacket(p.getUniqueId(), 1);
                RedisClient.sendSectorsPacket(packet);
            }
            p.giveExp(DropManager.getExp(b.getType()));
            DropManager.getDropData(b.getType()).breakBlock(b, p, p.getItemInHand());
            p.spigot().playEffect(b.getLocation(), Effect.FLAME, 1, 5, 0.2f, 0.3f, 0.2f, 0.1f, 10, 1000);
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {
        final Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        final Block block = e.getBlock();
        if (block.getType().equals(Material.STONE)) {
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 0.5f, (float)(Math.random() * 20.0) / 10.0f);
        }
        else if (block.getType().equals(Material.OBSIDIAN)) {
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 0.5f, (float)(Math.random() * 20.0) / 10.0f);
        }
    }
}
