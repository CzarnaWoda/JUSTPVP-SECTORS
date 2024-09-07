package pl.blackwater.enchantgui.data;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class LevelSettings {

    private final String id;
    private Enchantment enchantment;
    private ItemStack item;
    private int levels;
    private int row;
    private String name;
    private String lore;
    private int price;

    public LevelSettings(String id, Enchantment enchantment, ItemStack item, int levels, int row, String name, String lore, int price) {
        this.id = id;
        this.enchantment = enchantment;
        this.item = item;
        this.levels = levels;
        this.row = row;
        this.name = name;
        this.lore = lore;
        this.price = price;
    }
    public String getId(){
    	return id;
    }
    public Enchantment getEnchantment(){
    	return enchantment;
    }
    public ItemStack getItem(){
    	return item;
    }
    public int getLevels(){
    	return levels;
    }
    public int getRow(){
    	return row;
    }
    public String getName(){
    	return name;
    }
    public String getLore(){
    	return lore;
    }
    public int getPrice(){
    	return price;
    }

}
