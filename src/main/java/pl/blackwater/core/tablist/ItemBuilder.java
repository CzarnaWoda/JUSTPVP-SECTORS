package pl.blackwater.core.tablist;


import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;

public class ItemBuilder
{
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(final Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(final Material material, final int stack) {
        this.itemStack = new ItemStack(material, stack);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(final Material material, final int stack, final int data) {
        this.itemStack = new ItemStack(material, stack, (short)data);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public void refreshMeta() {
        this.itemStack.setItemMeta(this.itemMeta);
    }

    public ItemBuilder setName(final String name, final boolean color) {
        this.itemMeta.setDisplayName(color ? StringUtils.colored(name) : name);
        this.refreshMeta();
        return this;
    }

    public ItemBuilder setLore(final List<String> lore) {
        final List<String> formatted = new ArrayList<>();
        for (final String str : lore) {
            formatted.add(StringUtils.colored(str));
        }
        this.itemMeta.setLore(formatted);
        this.refreshMeta();
        return this;
    }

    public ItemBuilder setLore(final String... lore) {
        return this.setLore(Arrays.asList(lore));
    }

    public ItemBuilder addEnchant(final Enchantment enchant, final int level) {
        this.itemMeta.addEnchant(enchant, level, true);
        this.refreshMeta();
        return this;
    }

    public ItemBuilder setFlag(final ItemFlag flag) {
        this.itemMeta.addItemFlags(flag);
        this.refreshMeta();
        return this;
    }

    public ItemStack getItem() {
        return this.itemStack;
    }

    public ItemMeta getMeta() {
        return this.itemMeta;
    }
}
