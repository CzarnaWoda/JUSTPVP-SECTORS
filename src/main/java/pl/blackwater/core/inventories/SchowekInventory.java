package pl.blackwater.core.inventories;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.utils.ColoredMaterialsUtil;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemStackUtil;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchowekInventory implements Colors{
	
	
	public static InventoryView openMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + UnderLined + "Schowek" + SpecialSigns + " <<")), 3);
		final User u = UserManager.getUser(player);
		final ItemBuilder kox = new ItemBuilder(Material.GOLDEN_APPLE,1,(short)1).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit koxow: " + ImportantColor + CoreConfig.LIMITMANAGER_KOX)))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "W schowku: " + WarningColor + u.getSafeKox())))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij " + ImportantColor + "PPM" + MainColor + " aby wyplacic!")));
		final ItemBuilder ref = new ItemBuilder(Material.GOLDEN_APPLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit refili: " + ImportantColor + CoreConfig.LIMITMANAGER_REF)))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "W schowku: " + WarningColor + u.getSafeRef())))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij " + ImportantColor + "PPM" + MainColor + " aby wyplacic!")));
		final ItemBuilder pearl = new ItemBuilder(Material.ENDER_PEARL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit perel: " + ImportantColor + CoreConfig.LIMITMANAGER_PEARL)))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "W schowku: " + WarningColor + u.getSafePearl())))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij " + ImportantColor + "PPM" + MainColor + " aby wyplacic!")));
		final ItemBuilder snowball = new ItemBuilder(Material.SNOW_BALL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit sniezek: " + ImportantColor + CoreConfig.LIMITMANAGER_SNOWBALL)))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "W schowku: " + WarningColor + u.getSafeSnowBall())))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij " + ImportantColor + "PPM" + MainColor + " aby wyplacic!")));
		final ItemBuilder arrow = new ItemBuilder(Material.ARROW).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit strzal: " + ImportantColor + CoreConfig.LIMITMANAGER_ARROWS)))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "W schowku: " + WarningColor + u.getSafeArrow())))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij " + ImportantColor + "PPM" + MainColor + " aby wyplacic!")));
		final ItemBuilder fill = new ItemBuilder(Material.HOPPER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " +  MainColor + "DOBIERZ" + ImportantColor + " LIMIT " + SpecialSigns + "<<-")));
		final ItemBuilder purple = ColoredMaterialsUtil.getStainedGlassPane((short)10);
		final ItemBuilder red = ColoredMaterialsUtil.getStainedGlassPane((short)14);
		inv.setItem(0, purple.build(), null);
		inv.setItem(1, red.build(), null);
		inv.setItem(2, purple.build(), null);
		inv.setItem(3, purple.build(), null);
		inv.setItem(4, fill.build(), (p, paramInventory, paramInt, paramItemStack) -> {
			final int koxs = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE,p,(short)1);
			final int pearls = ItemStackUtil.getAmountOfItem(Material.ENDER_PEARL,p,(short)0);
			final int refs = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE,p,(short)0);
			final int arrows = ItemStackUtil.getAmountOfItem(Material.ARROW,p,(short)0);
			final int snowballs = ItemStackUtil.getAmountOfItem(Material.SNOW_BALL,p,(short)0);


			final int koxadd = CoreConfig.LIMITMANAGER_KOX - koxs;
			final int refadd = CoreConfig.LIMITMANAGER_REF - refs;
			final int pearladd = CoreConfig.LIMITMANAGER_PEARL - pearls;
			final int arrowadd = CoreConfig.LIMITMANAGER_ARROWS - arrows;
			final int snowadd = CoreConfig.LIMITMANAGER_SNOWBALL - snowballs;
			final List<ItemStack> items = new ArrayList<>();
			if(koxadd > 0 && u.getSafeKox() >= koxadd){
				items.add(new ItemStack(Material.GOLDEN_APPLE,koxadd,(short)1));
				u.removeSafeKox(koxadd);
			}
			if(refadd > 0 && u.getSafeRef() >= refadd){
				items.add(new ItemStack(Material.GOLDEN_APPLE,refadd,(short)0));
				u.removeSafeRef(refadd);
			}
			if(pearladd > 0 && u.getSafePearl() >= pearladd){
				items.add(new ItemStack(Material.ENDER_PEARL,pearladd,(short)0));
				u.removeSafePearl(pearladd);
			}
			if(arrowadd > 0 && u.getSafePearl() >= pearladd){
				items.add(new ItemStack(Material.ARROW,arrowadd,(short)0));
				u.removeSafeArrow(arrowadd);
			}
			if(snowadd > 0 && u.getSafeSnowBall() >= snowadd){
				items.add(new ItemStack(Material.SNOW_BALL,snowadd,(short)0));
				u.removeSafeSnowBall(snowadd);
			}
			if(items.size() > 0) {
				ItemUtil.giveItems(items, p);
			}
			p.closeInventory();
			Util.sendMsg(p,Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Dobrano " + ImportantColor + " limit!")));
		});
		inv.setItem(5, purple.build(), null);
		inv.setItem(6, purple.build(), null);
		inv.setItem(7, red.build(), null);
		inv.setItem(8, purple.build(), null);
		inv.setItem(9, red.build(), null);
		inv.setItem(10, kox.build(), (p, arg1, arg2, item) -> {
			if(CoreConfig.ENABLEMANAGER_SAFE) {
				final int amount = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE,p,(short)1);
				if(amount >= CoreConfig.LIMITMANAGER_KOX){
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz juz limit przy sobie!"));
					return;
				}
				if(u.getSafeKox() != 0) {
					List<ItemStack> items = Collections.singletonList(new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1));
					u.removeSafeKox(1);
					ItemUtil.giveItems(items, p);
					ItemMeta meta = item.getItemMeta();
					List<String> lore = meta.getLore();
					lore.set(0, Util.fixColor(Util.replaceString("  * " + MainColor + "W schowku: " + WarningColor + u.getSafeKox())));
					meta.setLore(lore);
					item.setItemMeta(meta);
					Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Wyplaciles z schowka " + ChatColor.GOLD + "KOX x1")));
				}else
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz za malo koxow w depozycie!"));
				}else
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Schowek jest wylaczony!"));
			});
		inv.setItem(11, red.build(), null);
		inv.setItem(12, red.build(), null);
		inv.setItem(13, pearl.build(), (p, arg1, arg2, item) -> {
			if(CoreConfig.ENABLEMANAGER_SAFE) {
				final int amount = ItemStackUtil.getAmountOfItem(Material.ENDER_PEARL,p,(short)0);
				if(amount >= CoreConfig.LIMITMANAGER_PEARL){
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz juz limit przy sobie!"));
					return;
				}
				if(u.getSafePearl() != 0) {
					List<ItemStack> items = Collections.singletonList(new ItemStack(Material.ENDER_PEARL, 1));
					u.removeSafePearl(1);
					ItemUtil.giveItems(items, p);
					ItemMeta meta = item.getItemMeta();
					List<String> lore = meta.getLore();
					lore.set(0, Util.fixColor(Util.replaceString("  * " + MainColor + "W schowku: " + WarningColor + u.getSafePearl())));
					meta.setLore(lore);
					item.setItemMeta(meta);
					Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Wyplaciles z schowka " + ChatColor.LIGHT_PURPLE + "PERLA x1")));
				}else
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz za malo perel w depozycie!"));
				}else
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Schowek jest wylaczony!"));
			});
		inv.setItem(14, red.build(), null);
		inv.setItem(15, red.build(), null);
		inv.setItem(16, ref.build(), (p, arg1, arg2, item) -> {
			if(CoreConfig.ENABLEMANAGER_SAFE) {
				final int amount = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE,p,(short)0);
				if(amount >= CoreConfig.LIMITMANAGER_REF){
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz juz limit przy sobie!"));
					return;
				}
				if(u.getSafeRef() != 0) {
					List<ItemStack> items = Collections.singletonList(new ItemStack(Material.GOLDEN_APPLE, 1));
					u.removeSafeRef(1);
					ItemUtil.giveItems(items, p);
					ItemMeta meta = item.getItemMeta();
					List<String> lore = meta.getLore();
					lore.set(0, Util.fixColor(Util.replaceString("  * " + MainColor + "W schowku: " + WarningColor + u.getSafeRef())));
					meta.setLore(lore);
					item.setItemMeta(meta);
					Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Wyplaciles z schowka " + ChatColor.YELLOW + "REFIL x1")));
				}else
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz za malo refili w depozycie!"));
				}else
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Schowek jest wylaczony!"));
			});
		inv.setItem(17, red.build(), null);
		inv.setItem(18, arrow.build(), (p, arg1, arg2, item) -> {
			if(CoreConfig.ENABLEMANAGER_SAFE){
				final int amount = ItemStackUtil.getAmountOfItem(Material.ARROW,p,(short)0);
				if(amount >= CoreConfig.LIMITMANAGER_ARROWS){
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz juz limit przy sobie!"));
					return;
				}
				if(u.getSafeArrow() > 0){
					List<ItemStack> items = Collections.singletonList(new ItemStack(Material.ARROW, 1));
					u.removeSafeArrow(1);
					ItemUtil.giveItems(items, p);
					ItemMeta meta = item.getItemMeta();
					List<String> lore = meta.getLore();
					lore.set(0, Util.fixColor(Util.replaceString("  * " + MainColor + "W schowku: " + WarningColor + u.getSafeArrow())));
					meta.setLore(lore);
					item.setItemMeta(meta);
					Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Wyplaciles z schowka " + ChatColor.WHITE + "ARROW x1")));
				}else
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz za malo strzal w depozycie!"));
			}else
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Schowek jest wylaczony!"));
		});
		inv.setItem(19, red.build(), null);
		inv.setItem(20, purple.build(), null);
		inv.setItem(21, purple.build(), null);
		inv.setItem(22, red.build(), null);
		inv.setItem(23, purple.build(), null);
		inv.setItem(24, purple.build(), null);
		inv.setItem(25, red.build(), null);
		inv.setItem(26, snowball.build(), (p, arg1, arg2, item) -> {
			if(CoreConfig.ENABLEMANAGER_SAFE) {
				final int amount = ItemStackUtil.getAmountOfItem(Material.SNOW_BALL,p,(short)0);
				if(amount >= CoreConfig.LIMITMANAGER_SNOWBALL){
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz juz limit przy sobie!"));
					return;
				}
				if(u.getSafeSnowBall() != 0) {
					List<ItemStack> items = Collections.singletonList(new ItemStack(Material.SNOW_BALL, 1));
					u.removeSafeSnowBall(1);
					ItemUtil.giveItems(items, p);
					ItemMeta meta = item.getItemMeta();
					List<String> lore = meta.getLore();
					lore.set(0, Util.fixColor(Util.replaceString("  * " + MainColor + "W schowku: " + WarningColor + u.getSafeSnowBall())));
					meta.setLore(lore);
					item.setItemMeta(meta);
					Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Wyplaciles z schowka " + ChatColor.DARK_BLUE + "SNIEZKA x1")));
				}else
					Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Masz za malo sniezek w depozycie!"));
			}else
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Schowek jest wylaczony!"));
		});
		return player.openInventory(inv.get());
	}
}
