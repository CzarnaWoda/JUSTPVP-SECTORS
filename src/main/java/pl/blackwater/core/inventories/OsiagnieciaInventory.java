package pl.blackwater.core.inventories;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.Osiagniecie;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.OsiagnieciaConfig;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.MathUtil;
import pl.blackwaterapi.utils.Util;

public class OsiagnieciaInventory implements Colors{
	
	public static InventoryView openMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_INVENTORYNAME)), 3);
		final ItemBuilder BREAK_STONE = new ItemBuilder(Material.DIAMOND_PICKAXE).setTitle(Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_ITEMNAME_BREAKSTONE)));
		final ItemBuilder EAT_KOX = new ItemBuilder(Material.GOLDEN_APPLE,1,(short)1).setTitle(Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_ITEMNAME_EATKOX)));
		final ItemBuilder KILLS = new ItemBuilder(Material.DIAMOND_SWORD,1).setTitle(Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_ITEMNAME_KILLS)));
		final ItemBuilder OPEN_CHEST = new ItemBuilder(Material.CHEST,1).setTitle(Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_ITEMNAME_OPENCHEST)));
		final ItemBuilder THROW_PEARL = new ItemBuilder(Material.ENDER_PEARL,1).setTitle(Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_ITEMNAME_THROWPEARL)));
		final ItemBuilder EAT_REFIL = new ItemBuilder(Material.GOLDEN_APPLE,1).setTitle(Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_ITEMNAME_EATREFIL)));
		final ItemBuilder SPEND_TIME = new ItemBuilder(Material.WATCH).setTitle(Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_ITEMNAME_SPENDTIME)));
		final ItemBuilder green = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)13).setTitle(Util.fixColor("&6"));
		final ItemBuilder black = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)15).setTitle(Util.fixColor("&6"));
		final ItemBuilder orange = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)1).setTitle(Util.fixColor("&6"));
		final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)2).setTitle(Util.fixColor("&6"));
		final ItemBuilder brown = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)12).setTitle(Util.fixColor("&6"));
		final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle(Util.fixColor("&6"));
		final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(Util.fixColor("&6"));
		final ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)7).setTitle(Util.fixColor("&6"));
		final User u = UserManager.getUser(player);
		inv.setItem(0, air.build(),null);
		inv.setItem(9, air.build(),null);
		inv.setItem(9+9, air.build(),null);
		inv.setItem(10-9, black.build(),null);
		inv.setItem(10, EAT_KOX.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openSecondMenu(p, OsiagnieciaConfig.EAT_KOX, u.getEatKox());
		});
		inv.setItem(10+9, black.build(),null);
		inv.setItem(11-9, orange.build(),null);
		inv.setItem(11, KILLS.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openSecondMenu(p, OsiagnieciaConfig.KILLS, u.getKills());
		});
		inv.setItem(11+9, orange.build(),null);
		inv.setItem(12-9, cyan.build(),null);
		inv.setItem(12, OPEN_CHEST.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openSecondMenu(p, OsiagnieciaConfig.OPEN_CHEST, u.getOpenPremiumChest());
		});
		inv.setItem(12+9, cyan.build(),null);
		inv.setItem(13-9, brown.build(),null);
		inv.setItem(13, THROW_PEARL.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openSecondMenu(p, OsiagnieciaConfig.THROW_PEARL, u.getThrowPearl());
		});
		inv.setItem(13+9, brown.build(),null);
		inv.setItem(14-9, purple.build(),null);
		inv.setItem(14, EAT_REFIL.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openSecondMenu(p, OsiagnieciaConfig.EAT_REFIL, u.getEatRef());
		});
		inv.setItem(14+9, purple.build(),null);
		inv.setItem(15-9, blue.build(),null);
		inv.setItem(15, BREAK_STONE.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openSecondMenu(p, OsiagnieciaConfig.BREAK_STONE, u.getDropStones());
		});
		inv.setItem(15+9, blue.build(),null);
		inv.setItem(16-9, green.build(),null);
		inv.setItem(16,SPEND_TIME.build(),(p, inventory, i, itemStack) -> {
			p.closeInventory();
			openSecondMenu(p, OsiagnieciaConfig.SPEND_TIME, u.getTimePlay());
		});
		inv.setItem(16+9, green.build(),null);
		inv.setItem(17-9, air.build(),null);
		inv.setItem(17, air.build(),null);
		inv.setItem(17+9, air.build(),null);
		return player.openInventory(inv.get());
	}
	private static void openSecondMenu(Player player, List<Osiagniecie> osiagniecia, int value) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(OsiagnieciaConfig.GUI_INVENTORYNAME)), 3);
		int position = 9;
		for(Osiagniecie o : osiagniecia) {
			final int amount = o.getAmount_toachivment();
			final double procent = MathUtil.round((double)value / amount * 100.0D, 1);
			final ItemBuilder glass = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)(value < amount ? 14 : 5)).setTitle(Util.fixColor(Util.replaceString((value < amount ? ChatColor.DARK_RED + "%X%" : ChatColor.GREEN + "%V%"))));
			inv.setItem(position - 9, glass.build(), null);
			inv.setItem(position, o.getGuiItem(procent, value), (player1, inventory, i, itemStack) -> {
				final User user = UserManager.getUser(player);
				if(user.getTakenAchievement().contains(o.getId())){
					Util.sendMsg(player, Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Odebrales juz to osiagniecie!"));
				}else if(value >= amount){
					user.getTakenAchievement().add(o.getId());
					Osiagniecie.Complete(player, o);
					user.update(true);
				}else{
					Util.sendMsg(player, Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Osiagniecie nie zostalo ukonczone!"));
				}
			});
			inv.setItem(position + 9, glass.build(), null);
			position++;
		}
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		final ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(Util.fixColor("&6"));
		inv.setItem(17-9, air.build(), null);
		inv.setItem(17, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenu(p);
		});
		inv.setItem(17+9, air.build(), null);
		player.openInventory(inv.get());
	}

}
