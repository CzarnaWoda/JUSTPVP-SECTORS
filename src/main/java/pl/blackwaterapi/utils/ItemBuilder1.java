package pl.blackwaterapi.utils;


import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

public class ItemBuilder1
{
  private ItemStack is;
  
  public ItemBuilder1(Material m)
  {
    this(m, 1);
  }
  
  public ItemBuilder1(ItemStack is)
  {
    this.is = is;
  }
  
  public ItemBuilder1(Material m, int amount)
  {
    this.is = new ItemStack(m, amount);
  }
  
  public ItemBuilder1(Material m, int amount, short data)
  {
    this.is = new ItemStack(m, amount, data);
  }
  
  public ItemBuilder1(Material m, short data)
  {
    this.is = new ItemStack(m, data);
  }
  
  public ItemBuilder1 setDurability(short dur)
  {
    this.is.setDurability(dur);
    return this;
  }
  
  public ItemBuilder1 setName(String name)
  {
    ItemMeta im = this.is.getItemMeta();
    im.setDisplayName(name);
    this.is.setItemMeta(im);
    return this;
  }
  
  public ItemBuilder1 setMaterialData(MaterialData data)
  {
    this.is.setData(data);
    return this;
  }
  
  public ItemBuilder1 addUnsafeEnchantment(Enchantment ench, int level)
  {
    this.is.addUnsafeEnchantment(ench, level);
    return this;
  }
  
  public ItemBuilder1 removeEnchantment(Enchantment ench)
  {
    this.is.removeEnchantment(ench);
    return this;
  }
  
  public ItemBuilder1 setSkullOwner(String owner)
  {
    try
    {
      SkullMeta im = (SkullMeta)this.is.getItemMeta();
      im.setOwner(owner);
      this.is.setItemMeta(im);
    }
    catch (ClassCastException ignored) {}
    return this;
  }
  
  public ItemBuilder1 addEnchant(Enchantment ench, int level)
  {
    ItemMeta im = this.is.getItemMeta();
    im.addEnchant(ench, level, true);
    this.is.setItemMeta(im);
    return this;
  }
  
  @SuppressWarnings("deprecation")
public ItemBuilder1 setWoolColor(DyeColor color)
  {
    if (!this.is.getType().equals(Material.WOOL)) {
      return this;
    }
    this.is.setDurability(color.getData());
    return this;
  }
  
  public ItemBuilder1 setInfinityDurability()
  {
    this.is.setDurability(Short.MAX_VALUE);
    return this;
  }
  
  public ItemBuilder1 setLore(String... lore)
  {
    ItemMeta im = this.is.getItemMeta();
    im.setLore(Arrays.asList(lore));
    this.is.setItemMeta(im);
    return this;
  }
  
  public ItemBuilder1 setLore(List<String> lore)
  {
    ItemMeta im = this.is.getItemMeta();
    im.setLore(lore);
    this.is.setItemMeta(im);
    return this;
  }
  
  public ItemStack toItemStack()
  {
    return this.is;
  }
}
