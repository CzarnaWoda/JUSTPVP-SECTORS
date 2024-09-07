package pl.blackwaterapi.gui.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import pl.blackwater.core.managers.TeleportManager;
import pl.blackwaterapi.gui.actions.IAction;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryListener implements Listener {


    private static final Map<Inventory, InventoryGUI> inventories = new HashMap<>();
    private final HashMap<UUID, Long> times = new HashMap<>();

    @EventHandler
    public void onClick(final InventoryClickEvent e){
        final Player player = (Player) e.getWhoClicked();
        if(TeleportManager.hasJoinDelayRequest(player.getUniqueId())) {
            Util.sendMsg(player, " &8» &cNie mozesz klikac w inventory podczas teleportacji!");
            e.setCancelled(true);
            return;
        }
        if(TeleportManager.hasTeleportRequest(player.getUniqueId()))
        {
            Util.sendMsg(player, " &8» &cNie mozesz klikac w inventory podczas teleportacji!");
            e.setCancelled(true);
            return;
        }
        InventoryGUI gui = inventories.get(e.getInventory());
        if(gui == null) return;
        e.setCancelled(true);
        IAction action = gui.getActions().get(e.getRawSlot());
        if (action != null) {
            if(!player.hasPermission("core.admin") && times.get(player.getUniqueId()) != null && times.get(player.getUniqueId()) > System.currentTimeMillis()){
                Util.sendMsg(player, Util.fixColor(Util.replaceString("&4Blad: &cNie mozesz tak czesto klikac !")));
            }else {
                times.put(player.getUniqueId(),System.currentTimeMillis() + 500L);
                action.execute(player, e.getInventory(), e.getRawSlot(), e.getInventory().getItem(e.getRawSlot()));
            }
        }

    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event){
        InventoryGUI gui = inventories.get(event.getInventory());
        if(gui == null) return;
        inventories.remove(event.getInventory());
    }

    public static Map<Inventory, InventoryGUI> getInventories() {
        return inventories;
    }
}
