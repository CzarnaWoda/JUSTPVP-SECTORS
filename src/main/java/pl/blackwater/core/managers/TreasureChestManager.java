package pl.blackwater.core.managers;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.TreasureChest;
import pl.blackwater.core.enums.ChestType;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.redis.channels.RedisChannel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TreasureChestManager {

    @Getter
    private HashMap<UUID, TreasureChest> chests = new HashMap<>();
    @Getter
    private List<Material> normalMaterialList = Arrays.asList(Material.DIAMOND, Material.IRON_INGOT, Material.GOLD_INGOT, Material.BOOK, Material.WOOD, Material.EXP_BOTTLE,Material.APPLE,Material.EMERALD,Material.DIAMOND, Material.IRON_INGOT, Material.GOLD_INGOT, Material.EXP_BOTTLE,Material.APPLE,Material.EMERALD,Material.SNOW_BALL);
    @Getter private List<Material> normalKitMaterialList = Arrays.asList(Material.IRON_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.IRON_HELMET, Material.CHAINMAIL_HELMET, Material.GOLD_HELMET, Material.LEATHER_HELMET, Material.IRON_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLD_LEGGINGS, Material.LEATHER_LEGGINGS,Material.IRON_BOOTS,Material.CHAINMAIL_BOOTS,Material.LEATHER_BOOTS, Material.GOLD_BOOTS, Material.IRON_SWORD, Material.GOLD_SWORD, Material.WOOD_SWORD, Material.STONE_SWORD);
    @Getter
    private HashMap<Material, Integer> maxEnchants = new HashMap<>();

    public TreasureChest getChest(UUID uuid){
        return chests.get(uuid);
    }

    public TreasureChest getChest(Location location){
        for(TreasureChest treasureChest : chests.values()){
            if(LocationUtil.convertLocationBlockToString(treasureChest.getLocation()).equalsIgnoreCase(LocationUtil.convertLocationBlockToString(location))){
                return treasureChest;
            }
        }
        return null;
    }

    public TreasureChest createChest(Block block, ChestType type, ItemStack[] inventory, long time){
        return new TreasureChest(UUID.randomUUID(), block.getLocation(),type,inventory,time);
    }



    public void registerTreasure(final TreasureChest treasureChest)
    {
        treasureChest.setup();
        chests.put(treasureChest.getUuid(), treasureChest);
    }

    public void deleteChest(TreasureChest treasureChest){
        chests.remove(treasureChest.getUuid());
    }

    public void setup() {
        maxEnchants.put(Material.IRON_CHESTPLATE,2);
        maxEnchants.put(Material.IRON_LEGGINGS,2);
        maxEnchants.put(Material.IRON_HELMET,2);
        maxEnchants.put(Material.IRON_BOOTS,2);
        maxEnchants.put(Material.LEATHER_CHESTPLATE,4);
        maxEnchants.put(Material.LEATHER_LEGGINGS,4);
        maxEnchants.put(Material.LEATHER_HELMET,4);
        maxEnchants.put(Material.LEATHER_BOOTS,4);
        maxEnchants.put(Material.CHAINMAIL_CHESTPLATE,3);
        maxEnchants.put(Material.CHAINMAIL_LEGGINGS,3);
        maxEnchants.put(Material.CHAINMAIL_HELMET,3);
        maxEnchants.put(Material.CHAINMAIL_BOOTS,3);
        maxEnchants.put(Material.GOLD_CHESTPLATE,3);
        maxEnchants.put(Material.GOLD_LEGGINGS,3);
        maxEnchants.put(Material.GOLD_HELMET,3);
        maxEnchants.put(Material.GOLD_BOOTS,3);
        maxEnchants.put(Material.IRON_SWORD,2);
        maxEnchants.put(Material.GOLD_SWORD,3);
        maxEnchants.put(Material.STONE_SWORD,3);
        maxEnchants.put(Material.WOOD_SWORD,4);
        RedisChannel.INSTANCE.TREASURE_CHESTS.forEach(((uuid, s) -> {
            final TreasureChest chest = GsonUtil.fromJson(s, TreasureChest.class);
            chest.setup();
            chests.put(uuid, chest);
        }));
        Logger.info("Loaded " + chests.size() + " chests!");
    }
}
