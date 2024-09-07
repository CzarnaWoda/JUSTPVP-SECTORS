package pl.blackwater.chestpvpdrop.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import pl.blackwaterapi.utils.RandomUtil;


public class DropUtil
{
    public static void recalculateDurability(final Player player, final ItemStack item) {
        if (item.getType().getMaxDurability() != 0) {
            final int enchantLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
            final short d = item.getDurability();
            if (enchantLevel > 0) {
                if (100 / (enchantLevel + 1) > RandomUtil.getRandInt(0, 100)) {
                    if (d == item.getType().getMaxDurability()) {
                        player.getInventory().clear(player.getInventory().getHeldItemSlot());
                        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                    }
                    else {
                        item.setDurability((short)(d + 1));
                    }
                }
            }
            else if (d == item.getType().getMaxDurability()) {
                player.getInventory().clear(player.getInventory().getHeldItemSlot());
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
            }
            else {
                item.setDurability((short)(d + 1));
            }
        }
    }
    
    public static int addFortuneEnchant(final int amount, final ItemStack tool) {
        int a = amount;
        if (RandomUtil.getChance(30.0) && tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) >= 1) {
            a = amount + 1;
        }
        else if (RandomUtil.getChance(20.0) && tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) >= 2) {
            a = amount + 2;
        }
        else if (RandomUtil.getChance(10.0) && tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) >= 3) {
            a = amount + 3;
        }
        return a;
    }
    
    public static void addItemsToPlayer(final Player player, final List<?> items, final Block b) {
        final PlayerInventory inv = player.getInventory();
        final HashMap<?, ?> notStored = inv.addItem(items.toArray(new ItemStack[0]));
        for (final Map.Entry<?, ?> en : notStored.entrySet()) {
            b.getWorld().dropItemNaturally(b.getLocation(), (ItemStack)en.getValue());
        }
    }
}
