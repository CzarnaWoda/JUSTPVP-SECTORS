package pl.blackwater.enchantgui.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Enchantments {

    private static final Material[] enchantable = {
            Material.DIAMOND_PICKAXE, Material.DIAMOND_SWORD, Material.DIAMOND_SPADE, Material.DIAMOND_AXE, Material.DIAMOND_HOE,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.IRON_PICKAXE,
            Material.IRON_SWORD, Material.IRON_SPADE, Material.IRON_AXE, Material.IRON_HOE, Material.IRON_HELMET, Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.GOLD_PICKAXE, Material.GOLD_SWORD, Material.GOLD_SPADE, Material.GOLD_AXE,
            Material.GOLD_HOE, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.STONE_PICKAXE,
            Material.STONE_SWORD, Material.STONE_SPADE, Material.STONE_AXE, Material.STONE_HOE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.WOOD_PICKAXE, Material.WOOD_SWORD, Material.WOOD_SPADE,
            Material.WOOD_AXE, Material.WOOD_HOE, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS, Material.BOW, Material.FISHING_ROD};

    public static boolean isEnchantable(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return false;
        for (Material mat : enchantable) {
            if (item.getType().equals(mat)) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, Enchantment> enchants = new HashMap<>();

    static {
        enchants.put("alldamage", Enchantment.DAMAGE_ALL);
        enchants.put("alldmg", Enchantment.DAMAGE_ALL);
        enchants.put("sharpness", Enchantment.DAMAGE_ALL);
        enchants.put("sharp", Enchantment.DAMAGE_ALL);
        enchants.put("dal", Enchantment.DAMAGE_ALL);
        enchants.put("ardmg", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("baneofarthropod", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("dar", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);
        enchants.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchants.put("du", Enchantment.DAMAGE_UNDEAD);
        enchants.put("digspeed", Enchantment.DIG_SPEED);
        enchants.put("efficiency", Enchantment.DIG_SPEED);
        enchants.put("minespeed", Enchantment.DIG_SPEED);
        enchants.put("cutspeed", Enchantment.DIG_SPEED);
        enchants.put("ds", Enchantment.DIG_SPEED);
        enchants.put("eff", Enchantment.DIG_SPEED);
        enchants.put("durability", Enchantment.DURABILITY);
        enchants.put("dura", Enchantment.DURABILITY);
        enchants.put("unbreaking", Enchantment.DURABILITY);
        enchants.put("d", Enchantment.DURABILITY);
        enchants.put("thorns", Enchantment.THORNS);
        enchants.put("highcrit", Enchantment.THORNS);
        enchants.put("thorn", Enchantment.THORNS);
        enchants.put("highercrit", Enchantment.THORNS);
        enchants.put("t", Enchantment.THORNS);
        enchants.put("fireaspect", Enchantment.FIRE_ASPECT);
        enchants.put("fire", Enchantment.FIRE_ASPECT);
        enchants.put("meleefire", Enchantment.FIRE_ASPECT);
        enchants.put("meleeflame", Enchantment.FIRE_ASPECT);
        enchants.put("fa", Enchantment.FIRE_ASPECT);
        enchants.put("knockback", Enchantment.KNOCKBACK);
        enchants.put("kback", Enchantment.KNOCKBACK);
        enchants.put("kb", Enchantment.KNOCKBACK);
        enchants.put("k", Enchantment.KNOCKBACK);
        enchants.put("blockslootbonus", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("fort", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("lbb", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("mobslootbonus", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("mobloot", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("lbm", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("oxygen", Enchantment.OXYGEN);
        enchants.put("respiration", Enchantment.OXYGEN);
        enchants.put("breathing", Enchantment.OXYGEN);
        enchants.put("breath", Enchantment.OXYGEN);
        enchants.put("o", Enchantment.OXYGEN);
        enchants.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("protect", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("p", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("expprot", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("blastprotect", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("pe", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("fallprotection", Enchantment.PROTECTION_FALL);
        enchants.put("fallprot", Enchantment.PROTECTION_FALL);
        enchants.put("featherfall", Enchantment.PROTECTION_FALL);
        enchants.put("featherfalling", Enchantment.PROTECTION_FALL);
        enchants.put("pfa", Enchantment.PROTECTION_FALL);
        enchants.put("fireprotection", Enchantment.PROTECTION_FIRE);
        enchants.put("flameprotection", Enchantment.PROTECTION_FIRE);
        enchants.put("fireprotect", Enchantment.PROTECTION_FIRE);
        enchants.put("flameprotect", Enchantment.PROTECTION_FIRE);
        enchants.put("fireprot", Enchantment.PROTECTION_FIRE);
        enchants.put("flameprot", Enchantment.PROTECTION_FIRE);
        enchants.put("pf", Enchantment.PROTECTION_FIRE);
        enchants.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("projprot", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("pp", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("silktouch", Enchantment.SILK_TOUCH);
        enchants.put("softtouch", Enchantment.SILK_TOUCH);
        enchants.put("st", Enchantment.SILK_TOUCH);
        enchants.put("waterworker", Enchantment.WATER_WORKER);
        enchants.put("aquaaffinity", Enchantment.WATER_WORKER);
        enchants.put("watermine", Enchantment.WATER_WORKER);
        enchants.put("ww", Enchantment.WATER_WORKER);
        enchants.put("firearrow", Enchantment.ARROW_FIRE);
        enchants.put("flame", Enchantment.ARROW_FIRE);
        enchants.put("flamearrow", Enchantment.ARROW_FIRE);
        enchants.put("af", Enchantment.ARROW_FIRE);
        enchants.put("arrowdamage", Enchantment.ARROW_DAMAGE);
        enchants.put("power", Enchantment.ARROW_DAMAGE);
        enchants.put("arrowpower", Enchantment.ARROW_DAMAGE);
        enchants.put("ad", Enchantment.ARROW_DAMAGE);
        enchants.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
        enchants.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
        enchants.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchants.put("arrowpunch", Enchantment.ARROW_KNOCKBACK);
        enchants.put("ak", Enchantment.ARROW_KNOCKBACK);
        enchants.put("infinitearrows", Enchantment.ARROW_INFINITE);
        enchants.put("infarrows", Enchantment.ARROW_INFINITE);
        enchants.put("infinity", Enchantment.ARROW_INFINITE);
        enchants.put("infinite", Enchantment.ARROW_INFINITE);
        enchants.put("unlimited", Enchantment.ARROW_INFINITE);
        enchants.put("unlimitedarrows", Enchantment.ARROW_INFINITE);
        enchants.put("ai", Enchantment.ARROW_INFINITE);
    }


}
