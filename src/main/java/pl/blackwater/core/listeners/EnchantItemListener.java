package pl.blackwater.core.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantItemListener implements Listener{
	
	
	
	
    @EventHandler
    public void onEnchantItemEvent (EnchantItemEvent e) {
        if(e.getEnchantsToAdd().get(Enchantment.ARROW_INFINITE) != null && e.getEnchantsToAdd().get(Enchantment.ARROW_KNOCKBACK)  != null){
        	e.getEnchantsToAdd().remove(Enchantment.ARROW_INFINITE);
        }
        if(e.getEnchantsToAdd().get(Enchantment.DAMAGE_ALL) != null && e.getEnchantsToAdd().get(Enchantment.DAMAGE_ALL) > 4){
            e.getEnchantsToAdd().put(Enchantment.DAMAGE_ALL, 4);
        }
        if(e.getEnchantsToAdd().get(Enchantment.ARROW_DAMAGE) != null && e.getEnchantsToAdd().get(Enchantment.ARROW_DAMAGE) > 3){
            e.getEnchantsToAdd().put(Enchantment.ARROW_DAMAGE, 3);
        }
        if(e.getEnchantsToAdd().get(Enchantment.FIRE_ASPECT) != null && e.getEnchantsToAdd().get(Enchantment.FIRE_ASPECT) > 1){
            e.getEnchantsToAdd().put(Enchantment.FIRE_ASPECT, 1);
        }
        if(e.getEnchantsToAdd().get(Enchantment.KNOCKBACK) != null){
            e.getEnchantsToAdd().remove(Enchantment.KNOCKBACK);
        }
    }
}