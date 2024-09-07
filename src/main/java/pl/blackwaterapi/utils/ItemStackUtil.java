package pl.blackwaterapi.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtil
{
  public static int getAmountOfItem(Material material, Player player, short durability)
  {
    int amount = 0;
    ItemStack[] contents;
    int length = (contents = player.getInventory().getContents()).length;
    for (int i = 0; i < length; i++)
    {
      ItemStack itemStack = contents[i];
      if ((itemStack != null) && 
        (itemStack.getType().equals(material)) && 
        (itemStack.getDurability() == durability)) {
        amount += itemStack.getAmount();
      }
    }
    return amount;
  }
  
  public static int getAmountOfItem1(Material material, Player player, short durability)
  {
    int amount = 0;
    ItemStack[] contents;
    int length = (contents = player.getEnderChest().getContents()).length;
    for (int i = 0; i < length; i++)
    {
      ItemStack itemStack = contents[i];
      if ((itemStack != null) && 
        (itemStack.getType().equals(material)) && 
        (itemStack.getDurability() == durability)) {
        amount += itemStack.getAmount();
      }
    }
    return amount;
  }
  
  public static int remove(ItemStack base, Player player, int amount)
  {
    int actual = 0;
    int remaining = amount;
    ItemStack[] contents;
    int length = (contents = player.getInventory().getContents()).length;
    for (int i = 0; i < length; i++)
    {
      ItemStack itemStack = contents[i];
      if (actual == amount) {
        break;
      }
      if ((itemStack != null) && 
        (itemStack.getType().equals(base.getType())) && 
        (itemStack.getDurability() == base.getDurability())) {
        if (remaining == 0)
        {
          actual += itemStack.getAmount();
          player.getInventory().remove(itemStack);
        }
        else if (itemStack.getAmount() >= amount)
        {
          actual += itemStack.getAmount() - amount;
          itemStack.setAmount(amount);
          remaining = 0;
        }
        else
        {
          int add = itemStack.getAmount();
          remaining -= add;
          player.getInventory().remove(itemStack);
          actual += add;
        }
      }
    }
    return actual;
  }
}
