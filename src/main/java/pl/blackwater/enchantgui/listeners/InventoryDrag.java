package pl.blackwater.enchantgui.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import pl.blackwater.enchantgui.TemporaryStorage;
import pl.blackwater.enchantgui.configuration.Settings;
import pl.blackwater.enchantgui.utils.Util;

public class InventoryDrag implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getView().getTopInventory();

        if (inventory.getTitle().equals(Util.fixColors(Settings.firstTitle))
                || inventory.getTitle().equals(Util.fixColors(Settings.secondTitle))
                || inventory.getTitle().equals(Util.fixColors(Settings.thirdTitle))) {

            e.setCancelled(true);
            p.closeInventory();

            if (inventory.getTitle().equals(Util.fixColors(Settings.secondTitle))
                    || inventory.getTitle().equals(Util.fixColors(Settings.thirdTitle))) {
                TemporaryStorage.close(p);
            }

            Util.sendMessageFixed(p, Settings.closed);
        }
    }
}
