package pl.blackwater.enchantgui.data;

import org.bukkit.inventory.ItemStack;

public class EnchantmentSettings {

    private String id;
    private ItemStack item;
    private String name;
    private String lore;

    public EnchantmentSettings(String id, ItemStack item, String name, String lore) {
        this.id = id;
        this.item = item;
        this.name = name;
        this.lore = lore;
    }
    public String getId(){
    	return id;
    }
    public ItemStack getItem(){
    	return item;
    }
    public String getName(){
    	return name;
    }
    public String getLore(){
    	return lore;
    }
    public void setLore(String lore){
    	this.lore = lore;
    }
    public void setItem(ItemStack item){
    	this.item = item;
    }
    public void setName(String name){
    	this.name = name;
    }
    public void setId(String id){
    	this.id = id;
    }
}
