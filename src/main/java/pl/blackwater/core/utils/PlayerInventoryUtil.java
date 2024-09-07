package pl.blackwater.core.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import pl.blackwaterapi.utils.ItemBuilder1;

public class PlayerInventoryUtil
{
	public static void ClearPlayerInventory(final Player p)
	{
		final PlayerInventory inv = p.getInventory();
		inv.setArmorContents(new ItemStack[] {null,null,null,null});
		inv.setHeldItemSlot(0);
		inv.clear();
		p.updateInventory();
	}
	public static void DefaultPlayerInventory(final Player p) {
		p.getInventory().clear();
		final ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		final ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		final ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
		final ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		p.getInventory().addItem(new ItemBuilder1(Material.IRON_SWORD, 1).toItemStack(),
				new ItemBuilder1(Material.BOW, 1).toItemStack(),
				new ItemBuilder1(Material.COOKED_BEEF,64).toItemStack(),
				new ItemBuilder1(Material.ARROW, 16).toItemStack());
		p.getInventory().setArmorContents(new ItemStack[] {boots,leggings,chestplate,helmet});
		p.updateInventory();
	}

}
