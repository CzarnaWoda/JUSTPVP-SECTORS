package pl.blackwater.chestpvpdrop.drops;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;

import pl.blackwater.chestpvpdrop.data.DropData;
import pl.blackwater.chestpvpdrop.data.DropType;
import pl.blackwater.chestpvpdrop.utils.DropUtil;
import pl.blackwaterapi.utils.RandomUtil;

public class NormalDropData implements DropData
{
    
    public void breakBlock(Block block, Player player, ItemStack is)
    {
      List<ItemStack> drops = getDrops(block, is);
      DropUtil.addItemsToPlayer(player, drops, block);
      DropUtil.recalculateDurability(player, is);
      block.setType(Material.AIR);
    }
    
    private List<ItemStack> getDrops(final Block block, final ItemStack item) {
        final ArrayList<ItemStack> items = new ArrayList<>();
        final Material type = block.getType();
        int amount = 1;
        short data = block.getData();
        switch (SyntheticClass_1.$SwitchMap$org$bukkit$Material[type.ordinal()]) {
            case 1: {
                final NetherWarts warts = (NetherWarts)block.getState().getData();
                amount = (warts.getState().equals(NetherWartsState.RIPE) ? (RandomUtil.getRandInt(0, 2) + 2) : 1);
                items.add(new ItemStack(Material.NETHER_STALK, amount));
                break;
            }
            case 2: {
                final CocoaPlant plant = (CocoaPlant)block.getState().getData();
                amount = (plant.getSize().equals(CocoaPlant.CocoaPlantSize.LARGE) ? 3 : 1);
                items.add(new ItemStack(Material.INK_SACK, amount, (short)3));
                break;
            }
            case 3: {
                items.add(new ItemStack(Material.PUMPKIN_SEEDS, 1));
                break;
            }
            case 4: {
                items.add(new ItemStack(Material.MELON_SEEDS, 1));
                break;
            }
            case 5: {
                data = block.getState().getData().getData();
                switch (data) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6: {
                        amount = 1;
                        break;
                    }
                    case 7: {
                        amount = RandomUtil.getRandInt(1, 3);
                        break;
                    }
                }
                items.add(new ItemStack(Material.CARROT_ITEM, amount));
                break;
            }
            case 6: {
                data = block.getState().getData().getData();
                switch (data) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6: {
                        amount = 1;
                        break;
                    }
                    case 7: {
                        amount = RandomUtil.getRandInt(1, 3);
                        break;
                    }
                }
                items.add(new ItemStack(Material.POTATO_ITEM, amount));
                break;
            }
            case 7: {
                final Crops wheat = (Crops)block.getState().getData();
                int seedamount = 1;
                if (wheat.getState() == CropState.RIPE) {
                    items.add(new ItemStack(Material.WHEAT, RandomUtil.getRandInt(1, 2)));
                    seedamount = 1 + RandomUtil.getRandInt(0, 2);
                }
                items.add(new ItemStack(Material.SEEDS, seedamount));
                break;
            }
            case 8: {
                final byte amount2 = 1;
                items.add(new ItemStack(Material.SUGAR_CANE, amount2));
                break;
            }
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22: {
                items.addAll(block.getDrops(item));
                break;
            }
            default: {
                if (item.containsEnchantment(Enchantment.SILK_TOUCH) && block.getType().isBlock()) {
                    final boolean add = items.add(new ItemStack(block.getType(), 1, block.getData()));
                    break;
                }
                items.addAll(block.getDrops(item));
                break;
            }
        }
        return items;
    }
    
    @Override
    public DropType getDropType() {
        return DropType.NORMAL_DROP;
    }
    
    static class SyntheticClass_1
    {
        static final int[] $SwitchMap$org$bukkit$Material;
        
        static {
            $SwitchMap$org$bukkit$Material = new int[Material.values().length];
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.NETHER_WARTS.ordinal()] = 1;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.COCOA.ordinal()] = 2;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.PUMPKIN_STEM.ordinal()] = 3;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.MELON_STEM.ordinal()] = 4;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.CARROT.ordinal()] = 5;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.POTATO.ordinal()] = 6;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.CROPS.ordinal()] = 7;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.SUGAR_CANE_BLOCK.ordinal()] = 8;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.DOUBLE_PLANT.ordinal()] = 9;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.REDSTONE_WIRE.ordinal()] = 10;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.WOODEN_DOOR.ordinal()] = 11;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.IRON_DOOR.ordinal()] = 12;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.TRIPWIRE.ordinal()] = 13;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.LEVER.ordinal()] = 14;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.WOOD_BUTTON.ordinal()] = 15;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.STONE_BUTTON.ordinal()] = 16;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.DIODE_BLOCK_ON.ordinal()] = 17;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.DIODE_BLOCK_OFF.ordinal()] = 18;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.REDSTONE_COMPARATOR_OFF.ordinal()] = 19;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.REDSTONE_COMPARATOR_ON.ordinal()] = 20;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.DAYLIGHT_DETECTOR.ordinal()] = 21;
            }
            catch (NoSuchFieldError ignored) {}
            try {
                SyntheticClass_1.$SwitchMap$org$bukkit$Material[Material.REDSTONE_ORE.ordinal()] = 22;
            }
            catch (NoSuchFieldError ignored) {}
        }
    }
}
