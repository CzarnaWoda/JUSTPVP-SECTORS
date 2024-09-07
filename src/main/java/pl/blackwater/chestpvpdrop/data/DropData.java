package pl.blackwater.chestpvpdrop.data;

import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public interface DropData
{
    void breakBlock(Block p0, Player p1, ItemStack p2);
    
    DropType getDropType();
}
