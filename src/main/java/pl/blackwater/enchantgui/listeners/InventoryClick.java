package pl.blackwater.enchantgui.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.managers.ServerStatsManager;
import pl.blackwater.enchantgui.APIHandler;
import pl.blackwater.enchantgui.GUI;
import pl.blackwater.enchantgui.TemporaryStorage;
import pl.blackwater.enchantgui.configuration.Settings;
import pl.blackwater.enchantgui.data.LevelSettings;
import pl.blackwater.enchantgui.utils.Util;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        InventoryView view = e.getView();
        Inventory top = view.getTopInventory();
        int slot = e.getSlot();

        if (top.getTitle().equals(Util.fixColors(Settings.firstTitle))) {
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().equals(top)) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
                    e.setCancelled(true);
                    return;
                }

                TemporaryStorage.setItem(p, e.getCurrentItem());

                e.setCancelled(true);

                TemporaryStorage.goingThrough.add(p.getUniqueId());
                GUI.openSecond(p);
                TemporaryStorage.goingThrough.remove(p.getUniqueId());
            }
            else {
                e.setCancelled(true);
                p.closeInventory();
            }
        }

        if (top.getTitle().equals(Util.fixColors(Settings.secondTitle))) {
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().equals(top)) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
                if (slot > Settings.secondEnchantmentSettings.size() - 1) return;

                e.setCancelled(true);
                TemporaryStorage.setLevelSettings(p, slot);

                TemporaryStorage.goingThrough.add(p.getUniqueId());
                GUI.openThird(p, TemporaryStorage.getLevelSettings(p));
                TemporaryStorage.goingThrough.remove(p.getUniqueId());
            }
            else {
                e.setCancelled(true);
                p.closeInventory();
            }
        }

        if (top.getTitle().equals(Util.fixColors(Settings.thirdTitle))) {
            if (e.getClickedInventory() == null) return;
            if (e.getClickedInventory().equals(top)) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
                if (slot > TemporaryStorage.getLevelSettings(p).getLevels() - 1) return;

                e.setCancelled(true);

                LevelSettings levelSettings = TemporaryStorage.getLevelSettings(p);

                int level = slot + 1;
                double preprice = (p.hasPermission("sklep.vip") ? (int) (levelSettings.getPrice() * level * Settings.vipDiscount)
                        : levelSettings.getPrice() * level) - levelSettings.getPrice() * level * Math.min(0.5,(ServerStatsManager.getGlobalKills()*0.0005D)/100);
                int price = (int) preprice;

                if (!APIHandler.hasMoney(p, price)) {
                    Util.sendMessageFixed(p, Settings.noMoney
                            .replace("%difference%",
                                    String.valueOf(APIHandler.getDifference(p, price))));
                    return;
                }
                ItemStack itemStack1 = TemporaryStorage.getItem(p);
                if (itemStack1.getEnchantments().size() >= 3){
                	p.sendMessage(Util.fixColors(Settings.enchantsize));
                	return;
                }
                		if(itemStack1.getType().equals(Material.BOW)){
                			if (itemStack1.getEnchantments().size() >= 2){
                		p.sendMessage(Util.fixColors("&4Blad: &cNa luk mozna miec max 2 enchanty !"));
                		return;
                }
                		}

                APIHandler.removeMoney(p, price);

                ItemStack itemStack = TemporaryStorage.getItem(p);
                p.getInventory().removeItem(itemStack);
                Enchantment enchantment = levelSettings.getEnchantment();
                itemStack.addUnsafeEnchantment(enchantment, level);
                p.getInventory().addItem(itemStack);
                p.updateInventory();
                TemporaryStorage.close(p);

                Util.sendMessageFixed(p, Settings.success
                        .replace("%money%", String.valueOf(price))
                        .replace("%enchantment%", enchantment.getName().toLowerCase().replace("_", " "))
                        .replace("%level%", String.valueOf(level)));

                TemporaryStorage.goingThrough.add(p.getUniqueId());
                p.closeInventory();
                TemporaryStorage.goingThrough.remove(p.getUniqueId());
            }
            else {
                e.setCancelled(true);
                p.closeInventory();
            }
        }
    }
}
