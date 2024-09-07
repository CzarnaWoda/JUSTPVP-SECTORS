package pl.blackwater.enchantgui;

import org.bukkit.inventory.ItemStack;
import pl.blackwater.enchantgui.data.LevelSettings;

import java.util.UUID;

public class Storage {

    private final UUID uniqueId;
    private ItemStack item;
    private LevelSettings levelSettings;

    public Storage(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    public UUID getUniqueId(){
    	return uniqueId;
    }
    public ItemStack getItem(){
    	return item;
    }
    public LevelSettings getLevelSettings(){
    	return levelSettings;
    }
    public LevelSettings setLevelSettings(LevelSettings levelSettings2){
    	return this.levelSettings = levelSettings2;
    }
    public void setItem(ItemStack item){
    	this.item = item;
    }
}
