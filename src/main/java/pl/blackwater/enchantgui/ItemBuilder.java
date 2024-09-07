package pl.blackwater.enchantgui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.blackwater.enchantgui.utils.Util;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    public ItemStack getItemStack(){
    	return itemStack;
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, short data) {
        this.itemStack = new ItemStack(material, 1, data);
    }

    public ItemBuilder(Material material, int amount, int data) {
        this.itemStack = new ItemStack(material, amount, (short)data);
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Util.fixColors(Arrays.asList(lore)));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Util.fixColors(lore));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Util.fixColors(name));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(enchantment, level, false);
        itemStack.setItemMeta(meta);
        return this;
    }

}
