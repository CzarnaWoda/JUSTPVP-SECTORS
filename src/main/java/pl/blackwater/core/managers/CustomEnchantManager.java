package pl.blackwater.core.managers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.core.utils.EnchantAction;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder1;
import pl.blackwaterapi.utils.Util;

import java.util.HashMap;


public class CustomEnchantManager implements Listener
{
    private static HashMap<String, ItemStack> enchants;
    public static boolean isAllowedEnchant(ItemStack item, Enchantment e)
    {
        return e.canEnchantItem(item);
    }

    public static void generateEnchants(){
        enchants = new HashMap<>();


    }

    public static void openEnchant(Player p, int books, ItemStack item)
    {
        InventoryGUI gui = new InventoryGUI(Core.getPlugin(), Util.fixColor("&aAktywne biblioteczki: &a" + books), 6);


        gui.openInventory(p);
    }

    private static EnchantType getEnchantmentPartTypeForItemStack(ItemStack item)
    {
        switch (item.getType())
        {
            case LEATHER_HELMET:
            case IRON_HELMET:
            case GOLD_HELMET:
            case DIAMOND_HELMET:
            case LEATHER_CHESTPLATE:
            case IRON_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case IRON_LEGGINGS:
            case GOLD_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case LEATHER_BOOTS:
            case IRON_BOOTS:
            case GOLD_BOOTS:
            case DIAMOND_BOOTS:
                return EnchantType.ARMOR;
            case WOOD_PICKAXE:
            case STONE_PICKAXE:
            case IRON_PICKAXE:
            case GOLD_PICKAXE:
            case DIAMOND_PICKAXE:
            case WOOD_AXE:
            case STONE_AXE:
            case IRON_AXE:
            case GOLD_AXE:
            case DIAMOND_AXE:
            case WOOD_SPADE:
            case STONE_SPADE:
            case IRON_SPADE:
            case GOLD_SPADE:
            case DIAMOND_SPADE:
                return EnchantType.TOOL;
        }
        return EnchantType.OTHER;
    }

    private static EnchantType getEnchantTypeForItemStack(ItemStack item)
    {
        switch (item.getType())
        {
            case LEATHER_HELMET:
            case IRON_HELMET:
            case GOLD_HELMET:
            case DIAMOND_HELMET:
                return EnchantType.HEAD;
            case LEATHER_CHESTPLATE:
            case IRON_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
                return EnchantType.CHEST;
            case LEATHER_LEGGINGS:
            case IRON_LEGGINGS:
            case GOLD_LEGGINGS:
            case DIAMOND_LEGGINGS:
                return EnchantType.LEGS;
            case LEATHER_BOOTS:
            case IRON_BOOTS:
            case GOLD_BOOTS:
            case DIAMOND_BOOTS:
                return EnchantType.BOOTS;
            case WOOD_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case GOLD_SWORD:
            case DIAMOND_SWORD:
                return EnchantType.SWORD;
            case WOOD_PICKAXE:
            case STONE_PICKAXE:
            case IRON_PICKAXE:
            case GOLD_PICKAXE:
            case DIAMOND_PICKAXE:
                return EnchantType.PICKAXE;
            case BOW:
                return EnchantType.BOW;
        }
        return EnchantType.OTHER;
    }

    public enum EnchantType
    {
        HEAD,  CHEST,  LEGS,  BOOTS,  SWORD,  PICKAXE,  BOW,  OTHER,  ARMOR,  TOOL;

        EnchantType() {}
    }
}
