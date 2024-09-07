package pl.blackwaterapi.gui.actions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface IAction
{
  void execute(Player paramPlayer, Inventory paramInventory, int paramInt, ItemStack paramItemStack);
}
