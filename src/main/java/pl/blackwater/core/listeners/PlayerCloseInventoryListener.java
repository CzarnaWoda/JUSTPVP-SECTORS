package pl.blackwater.core.listeners;

import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pl.blackwater.core.Core;
import pl.blackwater.core.utils.TreasureChestUtil;

public class PlayerCloseInventoryListener implements Listener {

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent e) {
        final Inventory inventory = e.getInventory();
        final InventoryHolder inventoryHolder = inventory.getHolder();
        if (inventory.getHolder() != null) {
            if (inventory.getHolder() instanceof DoubleChest || inventoryHolder instanceof Chest) {
                if (Core.getTreasureChestManager().getChest(TreasureChestUtil.getLocation(inventoryHolder)) != null) {
                    if (e.getViewers().size() == 1) {
                        inventory.clear();
                    }
                }
            }
        }
    }
}
