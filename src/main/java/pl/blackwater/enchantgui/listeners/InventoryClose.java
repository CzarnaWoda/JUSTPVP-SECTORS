package pl.blackwater.enchantgui.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import pl.blackwater.enchantgui.TemporaryStorage;
import pl.blackwater.enchantgui.configuration.Settings;
import pl.blackwater.enchantgui.utils.Util;

public class InventoryClose implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inventory = e.getView().getTopInventory();

        if (TemporaryStorage.goingThrough.contains(p.getUniqueId()))
            return;

        if (inventory.getTitle().equals(Util.fixColors(Settings.firstTitle))
                || inventory.getTitle().equals(Util.fixColors(Settings.secondTitle))
                    || inventory.getTitle().equals(Util.fixColors(Settings.thirdTitle))) {

            if (inventory.getTitle().equals(Util.fixColors(Settings.secondTitle))
                    || inventory.getTitle().equals(Util.fixColors(Settings.thirdTitle))) {
                TemporaryStorage.close(p);
            }

            Util.sendMessageFixed(p, Settings.closed);
        }
    }
}
