package pl.blackwater.core.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashSet;

public class TreasureChestUtil {

    public static Location getLocation(InventoryHolder holder) {
        InventoryHolder invHolder = holder.getInventory().getHolder();
        if(invHolder instanceof DoubleChest) {
            return ((DoubleChest)invHolder).getLocation();
        }
        else if(holder instanceof BlockState) {
            return ((BlockState)holder).getLocation();
        }
        else {
            throw new IllegalArgumentException("Parameter holder must be a BlockState, or DoubleChest.");
        }
    }
    public static Block getTargetedContainerBlock(Player player) {
        Block block = player.getTargetBlock(getInvisibleBlocks(), 8);
        if(block == null || !(block.getState() instanceof InventoryHolder)) {
            return null;
        }
        else {
            return block;
        }
    }
    private static HashSet invisibleBlocks;
    private static HashSet<Material> getInvisibleBlocks() {
        if(invisibleBlocks == null) {
            invisibleBlocks  = new HashSet();
            Material[] mats = Material.values();
            for (Material mat : mats) {
                if(mat.isTransparent()) {
                    invisibleBlocks.add(mat);
                }
            }
        }

        return invisibleBlocks;
    }
}
