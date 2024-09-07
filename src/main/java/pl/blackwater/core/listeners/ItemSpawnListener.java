package pl.blackwater.core.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemSpawnListener implements Listener{
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemSpawn(ItemSpawnEvent e) {
		boolean a = e.getEntity().getItemStack().getType().equals(Material.IRON_HELMET);
		boolean b = e.getEntity().getItemStack().getType().equals(Material.IRON_CHESTPLATE);
		boolean c = e.getEntity().getItemStack().getType().equals(Material.IRON_LEGGINGS);
		boolean d = e.getEntity().getItemStack().getType().equals(Material.IRON_BOOTS);
		boolean f = e.getEntity().getItemStack().getType().equals(Material.IRON_SWORD);
		boolean g = e.getEntity().getItemStack().getType().equals(Material.BOW);
		boolean h = e.getEntity().getItemStack().getType().equals(Material.COOKED_BEEF);
		boolean i = e.getEntity().getItemStack().getType().equals(Material.ARROW);
		if(!(e.getEntity().getItemStack().getEnchantments().size() > 0)) {
			if(a || b || c || d || f || g || h || i) {
				e.getEntity().remove();
			}
		}

	}

}
