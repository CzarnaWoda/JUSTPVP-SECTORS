package pl.blackwater.enchantgui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.blackwater.core.managers.ServerStatsManager;
import pl.blackwater.enchantgui.configuration.Settings;
import pl.blackwater.enchantgui.data.EnchantmentSettings;
import pl.blackwater.enchantgui.data.LevelSettings;
import pl.blackwater.enchantgui.utils.Enchantments;
import pl.blackwater.enchantgui.utils.Util;

public class GUI {

    private static Inventory second;

    public static void openFirst(Player player) {
        Inventory first = Bukkit.createInventory(null, Settings.firstRows * 9, Util.fixColors(Settings.firstTitle));
        PlayerInventory playerInventory = player.getInventory();

        for (ItemStack item : playerInventory.getContents()) {
            if (Enchantments.isEnchantable(item)) {
                first.addItem(item.clone());
            }
        }
        player.openInventory(first);
    }

    public static void openSecond(Player player) {
        player.openInventory(second);
    }

    public static void openThird(Player player, LevelSettings levelSettings) {
        Inventory third = Bukkit.createInventory(null, Settings.thirdRows * 9, Util.fixColors(Settings.thirdTitle));
        for (int i = 0; i < levelSettings.getLevels(); i++) {
            double preprice = (player.hasPermission("sklep.vip") ? (int) (levelSettings.getPrice() * (i + 1) * Settings.vipDiscount)
                    : levelSettings.getPrice() * (i + 1)) - levelSettings.getPrice() * (i + 1) * Math.min(0.5,(ServerStatsManager.getGlobalKills()*0.0005D)/100);
            int price = (int) preprice;
            ItemBuilder builder = new ItemBuilder(levelSettings.getItem().clone());
            builder.setName(levelSettings.getName()
                    .replace("%level%", String.valueOf(i + 1))
                    .replace("%price%", String.valueOf(price)));
            builder.setLore(levelSettings.getLore()
                    .replace("%level%", String.valueOf(i + 1))
                    .replace("%price%", String.valueOf(price)));

            third.setItem(i + ((levelSettings.getRow() - 1) * 9), builder.getItemStack());
        }
        player.openInventory(third);
    }

    public static void initSecond() {
        second = Bukkit.createInventory(null, Settings.secondRows * 9, Util.fixColors(Settings.secondTitle));

        for (EnchantmentSettings enchantmentSettings : Settings.secondEnchantmentSettings.values()) {
            ItemBuilder builder = new ItemBuilder(enchantmentSettings.getItem().clone());
            builder.setName(enchantmentSettings.getName());
            builder.setLore(enchantmentSettings.getLore());

            second.addItem(builder.getItemStack());
        }
    }


}
