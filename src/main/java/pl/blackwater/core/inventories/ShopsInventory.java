package pl.blackwater.core.inventories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.lightshard.itemcases.ItemCases;
import net.md_5.bungee.api.ChatColor;
import pl.blackwater.bossx.utils.GUI;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.data.Shops;
import pl.blackwater.core.data.Shops.ShopBuyItems;
import pl.blackwater.core.data.TradeItems;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.ItemManager;
import pl.blackwater.core.managers.SpecialItemsManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.ShopConfig;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.*;

public class ShopsInventory implements Colors
{
	public static void openMainMenu(Player p)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Sklep" + ImportantColor + " serwera" + SpecialSigns + " *")), 6);
		final ItemBuilder gray = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)7).setTitle(Util.fixColor("&1"));
		final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle(Util.fixColor("&2"));
		final ItemBuilder blue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)11).setTitle(Util.fixColor("&3"));
		final ItemBuilder sell = new ItemBuilder(Material.GOLD_INGOT).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Sprzedawanie" + ImportantColor + " przedmiotow" + SpecialSigns + " *")));
		final ItemBuilder buy = new ItemBuilder(Material.IRON_INGOT).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Kupowanie" + ImportantColor + " przedmiotow" + SpecialSigns + " *")));
		final ItemBuilder boost = new ItemBuilder(Material.NETHER_STAR).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Kupowanie" + ImportantColor + " ulepszen" + SpecialSigns + " *")));
		final ItemBuilder enchantbuy = new ItemBuilder(Material.ENCHANTMENT_TABLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Kupowanie" + ImportantColor + " zaklec" + SpecialSigns + " *")));
		final ItemBuilder trade = new ItemBuilder(Material.DIAMOND_PICKAXE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Wymiana" + ImportantColor + " przedmiotow" + SpecialSigns + " *")));
		inv.setItem(0, gray.build(),null);
		inv.setItem(1, blue.build(),null);
		inv.setItem(2, gray.build(),null);
		inv.setItem(3, gray.build(),null);
		inv.setItem(4, gray.build(),null);
		inv.setItem(5, gray.build(),null);
		inv.setItem(6, gray.build(),null);
		inv.setItem(7, blue.build(),null);
		inv.setItem(8, gray.build(),null);
		inv.setItem(9, blue.build(),null);
		inv.setItem(10, sell.build(), (p1, arg1, arg2, arg3) -> {
			p1.closeInventory();
			openSellMenu(p1);
		});
		inv.setItem(11, blue.build(),null);
		inv.setItem(12, gray.build(),null);
		inv.setItem(13, gray.build(),null);
		inv.setItem(14, gray.build(),null);
		inv.setItem(15, blue.build(),null);
		inv.setItem(16, buy.build(), (p12, arg1, arg2, arg3) -> {
			p12.closeInventory();
			openBuyMenu(p12);
		});
		inv.setItem(17, blue.build(),null);
		inv.setItem(18, gray.build(),null);
		inv.setItem(19, purple.build(),null);
		inv.setItem(20, gray.build(),null);
		inv.setItem(21, gray.build(),null);
		inv.setItem(22, blue.build(),null);
		inv.setItem(23, gray.build(),null);
		inv.setItem(24, gray.build(),null);
		inv.setItem(25, purple.build(),null);
		inv.setItem(26, gray.build(),null);
		inv.setItem(27, gray.build(),null);
		inv.setItem(28, gray.build(),null);
		inv.setItem(29, gray.build(),null);
		inv.setItem(30, blue.build(),null);
		inv.setItem(31, boost.build(), (p13, arg1, arg2, arg3) -> {
			p13.closeInventory();
			openBoostMenu(p13);
		});
		inv.setItem(32, blue.build(),null);
		inv.setItem(33, gray.build(),null);
		inv.setItem(34, gray.build(),null);
		inv.setItem(35, gray.build(),null);
		inv.setItem(36, blue.build(),null);
		inv.setItem(37, gray.build(),null);
		inv.setItem(38, gray.build(),null);
		inv.setItem(39, gray.build(),null);
		inv.setItem(40, purple.build(),null);
		inv.setItem(41, gray.build(),null);
		inv.setItem(42, gray.build(),null);
		inv.setItem(43, gray.build(),null);
		inv.setItem(44, blue.build(),null);
		inv.setItem(45, enchantbuy.build(), (p14, arg1, arg2, arg3) -> {
			if(CoreConfig.CREATEMANAGER_ENCHANT_TIME > System.currentTimeMillis()) {
				p14.closeInventory();
				ActionBarUtil.sendActionBar(p14, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + WarningColor + "Enchantowanie przedmiotow bedzie mozliwe za: " + Util.secondsToString(((int)(CoreConfig.CREATEMANAGER_ENCHANT_TIME - System.currentTimeMillis()) / 1000)))));
				return;
			}
			p14.closeInventory();
			pl.blackwater.enchantgui.GUI.openFirst(p14);
		});
		inv.setItem(46, blue.build(),null);
		inv.setItem(47, gray.build(),null);
		inv.setItem(48, gray.build(),null);
		inv.setItem(49, gray.build(),null);
		inv.setItem(50, gray.build(),null);
		inv.setItem(51, gray.build(),null);
		inv.setItem(52, blue.build(),null);
		inv.setItem(53, trade.build(), (p15, arg1, arg2, arg3) -> {
			p15.closeInventory();
			openTradeMenu(p15);
		});
		p.openInventory(inv.get());
	}
	private static void openSellMenu(Player player)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(ShopConfig.sellguiname), ShopConfig.sellguirows);
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		final User u = UserManager.getUser(player);
		final ItemBuilder money = new ItemBuilder(Material.GOLD_NUGGET).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor +"Stan konta: ")))
					.addLore(Util.fixColor(Util.replaceString(ImportantColor + String.valueOf(u.getMoney()))));
		final ItemBuilder sellall = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + ImportantColor + "SPRZEDAJ WSZYSTKO " + SpecialSigns + "*")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Pamietaj aby skonfigurowac " + ImportantColor + "SPRZEDAWANIE WSZYSTKIEGO " + MainColor + "!")));
		final ItemBuilder sellallConfig = new ItemBuilder(Material.BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + ImportantColor + "KONFIGURACJA SELLALL " + SpecialSigns + "*")));
		final int size = inv.get().getSize();
		int position = 0;
		for(final Shops.ShopSellItems sell : ShopConfig.sellstorage.values())
		{
    		final ItemBuilder a = new ItemBuilder(sell.getItem().getType(),sell.getAmount(),(short)sell.getData()).setTitle(Util.fixColor(sell.getName()));
    		for(String s : ShopConfig.sellitemlore){
    			a.addLore(Util.fixColor(Util.replaceString(s))
    						.replace("{COST}", String.valueOf(sell.getSellmoney())));
    		}
    		inv.setItem(position, a.build(), (p, inv1, arg2, arg3) -> {
				final ItemStack sellItem = sell.getItemWithAll();
				List<ItemStack> sellItemList = Collections.singletonList(sellItem);
				if(!ItemUtil.checkAndRemove(sellItemList, p))
					  Util.sendMsg(p, Util.replaceString(ShopConfig.sellmessage_enoughtitem)
								  .replace("{AMOUNT}", String.valueOf(sell.getAmount()))
								  .replace("{ITEM}", ItemManager.get(sellItem.getType())));
				else{
					int money1 = sell.getSellmoney();
					  u.addMoneyViaPacket(money1);
					  Util.sendMsg(p, ShopConfig.sellmessage_finalsell.replace("{MONEY}", String.valueOf(money1)));
					final ItemStack item = inv1.getItem(size-2);
					final ItemMeta meta = item.getItemMeta();
					meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(ImportantColor + String.valueOf(u.getMoney())))));
					item.setItemMeta(meta);
				}
			});
    		position ++;
		}
		inv.setItem(size - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMainMenu(p);
		});
		inv.setItem(size - 2, money.build(), null);
		inv.setItem(size - 3, sellall.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
			int sellMoney = 0;
			for(Shops.ShopSellItems sell : ShopConfig.sellstorage.values()){
				if(u.getSellConfig().contains(sell.getItem().getType().name())){
					final int amount = ItemStackUtil.getAmountOfItem(sell.getItem().getType(),paramPlayer,(short)sell.getData());
					sellMoney = sellMoney + amount * sell.getSellmoney();
					final ItemStack itemStack = new ItemStack(sell.getItem().getType(),amount,(short)sell.getData());
					player.getInventory().forEach(item -> {
						if(item != null && item.getType().equals(itemStack.getType())){
							player.getInventory().removeItem(item);
						}
					});
				}
			}
			u.addMoneyViaPacket(sellMoney);
			Util.sendMsg(paramPlayer, SpecialSigns + "->> " + MainColor + "Sprzedales " + ImportantColor + "wszystkie " + MainColor + "przedmioty, uzyskales: " + ImportantColor + sellMoney + "$");
		});

		inv.setItem(size - 4, sellallConfig.build(), (paramPlayer, paramInventory, paramInt, paramItemStack) -> {
			final InventoryGUI configinv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString("&8|->> &2Konfiguracja SELLALL")), ShopConfig.sellguirows);
			int index = 0;
			for(Shops.ShopSellItems sell : ShopConfig.sellstorage.values()){
				final Material type = sell.getItem().getType();
				final ItemBuilder a = new ItemBuilder(type,1,(short)sell.getData()).setTitle(Util.fixColor(Util.replaceString("&8->> &6&n" + ItemManager.get(type).toUpperCase()  + " &8<<-")))
						.addLore(Util.fixColor(Util.replaceString("&8|->> &7Status: " + (u.getSellConfig().contains(type.name()) ? "&a%V%" : "&c%X%"))));
				configinv.setItem(index, a.build(),(paramPlayer1, paramInventory1, paramInt1, paramItemStack1) -> {
					if(u.getSellConfig().contains(type.name())){
						u.getSellConfig().remove(type.name());
					}else{
						u.getSellConfig().add(type.name());
					}
					final ItemMeta meta = paramItemStack1.getItemMeta();
					meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString("&8|->> &7Status: " + (u.getSellConfig().contains(type.name()) ? "&a%V%" : "&c%X%")))));
					paramItemStack1.setItemMeta(meta);
				});
				index++;
			}
			configinv.setItem(configinv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
				p.closeInventory();
				openSellMenu(p);
			});
			paramPlayer.closeInventory();
			configinv.openInventory(paramPlayer);
		});
		player.openInventory(inv.get());
	}
	private static void openBuyMenu(Player player)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(ShopConfig.buyguiname), ShopConfig.buyguirows);
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		final User u = UserManager.getUser(player);
		final ItemBuilder money = new ItemBuilder(Material.GOLD_NUGGET).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor +"Stan konta: ")))
					.addLore(Util.fixColor(Util.replaceString(ImportantColor + String.valueOf(u.getMoney()))));
		final int size = inv.get().getSize();
		int position = 0;
		for(final ShopBuyItems buy : ShopConfig.buystorage.values())
		{
			final ItemBuilder a = new ItemBuilder(buy.getItem().getType(),buy.getAmount(),(short)buy.getData()).setTitle(Util.fixColor(Util.replaceString(buy.getName())));
			for(String s : ShopConfig.buyitemlore)
				a.addLore(Util.fixColor(Util.replaceString(s)).replace("{COST}", String.valueOf(buy.getCost())));
			inv.setItem(position, a.build(), (p, inv1, slot, itemStack) -> {
				final int cost = buy.getCost();
				if(u.getMoney() < cost)
					Util.sendMsg(p, Util.fixColor(Util.replaceString(ShopConfig.buymessage_enoughtmoney)));
				else{
					u.removeMoneyViaPacket(cost);
					HashMap<Integer, ItemStack> notStored;
					notStored = p.getInventory().addItem(buy.getItemWithAll());
					if(itemStack.getType().equals(Material.TRIPWIRE_HOOK)){
						notStored = p.getInventory().addItem(ItemCases.getInstance().getCaseManager().fromName("ChestPvP").getKey());
					}
					for(final ItemStack i : notStored.values())
					{
						p.getWorld().dropItemNaturally(p.getLocation(), i);
					}
					Util.sendMsg(p, Util.fixColor(Util.replaceString(ShopConfig.buymessage_finalbuy)));
					final ItemStack item = inv1.getItem(size-2);
					final ItemMeta meta = item.getItemMeta();
					meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(ImportantColor + String.valueOf(u.getMoney())))));
					item.setItemMeta(meta);
				}

			});
			position++;
		}
		inv.setItem(size - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMainMenu(p);
		});
		inv.setItem(size - 2, money.build(), null);
		player.openInventory(inv.get());
	}
	private static void openBoostMenu(Player player)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(MainColor + "Kupowanie " + ImportantColor + "ulepszen")), 1);
		final ItemBuilder chest = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ulepszenie " + SpecialSigns + "* " + ImportantColor + UnderLined + "Magazynu")));
		final User u = UserManager.getUser(player);
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		final ItemBuilder money = new ItemBuilder(Material.GOLD_NUGGET).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor +"Stan konta: ")))
					.addLore(Util.fixColor(Util.replaceString(ImportantColor + String.valueOf(u.getMoney()))));
		final ItemBuilder goldingot = new ItemBuilder(Material.GOLD_INGOT).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ulepszenie " + SpecialSigns + "* " + ImportantColor + UnderLined + "UltraMoney")));
		final ItemBuilder goldenapple_1 = new ItemBuilder(Material.GOLDEN_APPLE,1,(short)1).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ulepszenie " + SpecialSigns + "* " + ImportantColor + UnderLined + "Grubasek")));
		final ItemBuilder diamondsword = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ulepszenie " + SpecialSigns + "* " + ImportantColor + UnderLined + "KoxPvP")));
		final ItemBuilder exp = new ItemBuilder(Material.EXP_BOTTLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ulepszenie " + SpecialSigns + "* " + ImportantColor + UnderLined + "UltraEXP")));
		final ItemBuilder goldenapple = new ItemBuilder(Material.GOLDEN_APPLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ulepszenie " + SpecialSigns + "* " + ImportantColor + UnderLined + "Mniejszy" + ChatColor.RED + " " + ImportantColor + UnderLined + "Grubasek")));
		final int size = inv.get().getSize();
		for(int i = 1; i < 7; i ++){
			final int chestcount = (i==1 ? 6 : (i==2 ? 10 : (i==3 ? 16 : (i==4 ? 20 : (i==5 ? 26 : 36)))));
			chest.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Ulepszenie " + ImportantColor + i + "/6" + SpecialSigns + " >> " + ImportantColor + Shops.getBoosts().get("CHEST_" + i) + "$")));
			chest.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + MainColor + "Mozesz postawic " + ImportantColor + BOLD  + chestcount + MainColor + " skrzynek w magazynie")));
			chest.addLore("");
		}
		chest.addLore("");
		chest.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Postep: " + ImportantColor + u.getBoostChest() + "/6")));
		inv.setItem(0, chest.build(), (j, arg1, arg2, arg3) -> {
			if(u.getBoostChest() >= 6){
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Masz juz wykupione to ulepszenie " + SpecialSigns + "(" + WarningColor_2 + "6/6" + SpecialSigns + ")")));
				return;
			}
			final int cost = Shops.getBoosts().get("CHEST_" + (u.getBoostChest() + 1));
			if(u.getMoney() < cost)
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz " + SpecialSigns + "(" + WarningColor_2 + cost + "$" + SpecialSigns + ")")));
			else{
				u.removeMoneyViaPacket(cost);
				u.addBoostChestViaPacket(1);
				Util.sendMsg(j, Util.fixColor(Util.replaceString(SpecialSigns + ">>" + MainColor + " Zakupiles ulepszenie " + ImportantColor + "Magazynu" + SpecialSigns + " * " + ImportantColor + u.getBoostChest())));
				j.closeInventory();
				openBoostMenu(j);
			}
		});
		for(int i = 1; i < 4;i ++)
		{
			final int ultramoney = (i == 1 ? 10 : (i == 2 ? 20 : 35));
			goldingot.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Ulepszenie " + ImportantColor + i + "/3" + SpecialSigns + " >> " + ImportantColor + Shops.getBoosts().get("ULTRAMONEY_" + i) + "$")));
			goldingot.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + MainColor + "Po zabiciu gracza dostaniesz dodatkowe " + ImportantColor + BOLD  + ultramoney + MainColor + "$")));
			goldingot.addLore("");
		}
		goldingot.addLore("");
		goldingot.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Postep: " + ImportantColor + u.getBoostMoney() + "/3")));
		inv.setItem(1, goldingot.build(), (j, arg1, arg2, arg3) -> {
			if(u.getBoostMoney() >= 3){
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Masz juz wykupione to ulepszenie " + SpecialSigns + "(" + WarningColor_2 + "3/3" + SpecialSigns + ")")));
				return;
			}
			final int cost = Shops.getBoosts().get("ULTRAMONEY_" + (u.getBoostMoney() + 1));
			if(u.getMoney() < cost)
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz " + SpecialSigns + "(" + WarningColor_2 + cost + "$" + SpecialSigns + ")")));
			else{
				u.removeMoneyViaPacket(cost);
				u.addBoostMoneyViaPacket(1);
				Util.sendMsg(j, Util.fixColor(Util.replaceString(SpecialSigns + ">>" + MainColor + " Zakupiles ulepszenie " + ImportantColor + "UltraMoney" + SpecialSigns + " * " + ImportantColor + u.getBoostMoney())));
				j.closeInventory();
				openBoostMenu(j);
			}
		});
		for(int i = 1; i < 4;i ++)
		{
			final String grubasek = (i == 1 ? "Speed I na 15 sekund" : (i == 2 ? "Speed I na 20 sekund, Fire Resistance I na 2 minuty" : "Speed I na 35 sekund, Fire Resistance I na 3 minuty"));
			goldenapple_1.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Ulepszenie " + ImportantColor + i + "/3" + SpecialSigns + " >> " + ImportantColor + Shops.getBoosts().get("GRUBASEK_" + i) + "$")));
			goldenapple_1.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + MainColor + "Po zjedzeniu koxa dostaniesz: ")));
			goldenapple_1.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + ImportantColor + BOLD  + grubasek)));
			goldenapple_1.addLore("");
		}
		goldenapple_1.addLore("");
		goldenapple_1.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Postep: " + ImportantColor + u.getBoostKox() + "/3")));
		inv.setItem(2, goldenapple_1.build(), (j, arg1, arg2, arg3) -> {
			if(u.getBoostKox() >= 3){
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Masz juz wykupione to ulepszenie " + SpecialSigns + "(" + WarningColor_2 + "3/3" + SpecialSigns + ")")));
				return;
			}
			final int cost = Shops.getBoosts().get("GRUBASEK_" + (u.getBoostKox() + 1));
			if(u.getMoney() < cost)
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz " + SpecialSigns + "(" + WarningColor_2 + cost + "$" + SpecialSigns + ")")));
			else{
				u.removeMoneyViaPacket(cost);
				u.addBoostKoxViaPacket(1);
				Util.sendMsg(j, Util.fixColor(Util.replaceString(SpecialSigns + ">>" + MainColor + " Zakupiles ulepszenie " + ImportantColor + "Grubasek" + SpecialSigns + " * " + ImportantColor + u.getBoostKox())));
				j.closeInventory();
				openBoostMenu(j);
			}
		});
		for(int i = 1; i < 4;i ++)
		{
			final String koxpvp = (i == 1 ? "Speed I na 5 sekund" : (i == 2 ? "Speed I na 10 sekund, Regeneration I na 5 sekund" : "Speed I na 15 sekund, Regeneration I na 10 sekund"));
			diamondsword.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Ulepszenie " + ImportantColor + i + "/3" + SpecialSigns + " >> " + ImportantColor + Shops.getBoosts().get("KOXPVP_" + i) + "$")));
			diamondsword.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + MainColor + "Po zabiciu gracza dostajesz: ")));
			diamondsword.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + ImportantColor + BOLD  + koxpvp)));
			diamondsword.addLore("");
		}
		diamondsword.addLore("");
		diamondsword.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Postep: " + ImportantColor + u.getBoostKill() + "/3")));
		inv.setItem(3, diamondsword.build(), (j, arg1, arg2, arg3) -> {
			if(u.getBoostKill() >= 3){
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Masz juz wykupione to ulepszenie " + SpecialSigns + "(" + WarningColor_2 + "3/3" + SpecialSigns + ")")));
				return;
			}
			final int cost = Shops.getBoosts().get("KOXPVP_" + (u.getBoostKill() + 1));
			if(u.getMoney() < cost)
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz " + SpecialSigns + "(" + WarningColor_2 + cost + "$" + SpecialSigns + ")")));
			else{
				u.removeMoneyViaPacket(cost);
				u.addBoostKillViaPacket(1);
				Util.sendMsg(j, Util.fixColor(Util.replaceString(SpecialSigns + ">>" + MainColor + " Zakupiles ulepszenie " + ImportantColor + "KoxPvP" + SpecialSigns + " * " + ImportantColor + u.getBoostKill())));
				j.closeInventory();
				openBoostMenu(j);
			}
		});
		for(int i = 1; i < 4;i ++)
		{
			final int ultraexp = (i == 1 ? 1 : (i == 2 ? 2 : 3));
			exp.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Ulepszenie " + ImportantColor + i + "/3" + SpecialSigns + " >> " + ImportantColor + Shops.getBoosts().get("ULTRAEXP_" + i) + "$")));
			exp.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + MainColor + "Po zabiciu gracza dostajesz " + ImportantColor +  BOLD + ultraexp + MainColor + " LvL EXP")));
			exp.addLore("");
		}
		exp.addLore("");
		exp.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Postep: " + ImportantColor + u.getBoostLvL() + "/3")));
		inv.setItem(4, exp.build(), (j, arg1, arg2, arg3) -> {
			if(u.getBoostLvL() >= 3){
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Masz juz wykupione to ulepszenie " + SpecialSigns + "(" + WarningColor_2 + "3/3" + SpecialSigns + ")")));
				return;
			}
			final int cost = Shops.getBoosts().get("ULTRAEXP_" + (u.getBoostLvL() + 1));
			if(u.getMoney() < cost)
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz " + SpecialSigns + "(" + WarningColor_2 + cost + "$" + SpecialSigns + ")")));
			else{
				u.removeMoneyViaPacket(cost);
				u.addBoostLvLViaPacket(1);
				Util.sendMsg(j, Util.fixColor(Util.replaceString(SpecialSigns + ">>" + MainColor + " Zakupiles ulepszenie " + ImportantColor + "UltraEXP" + SpecialSigns + " * " + ImportantColor + u.getBoostLvL())));
				j.closeInventory();
				openBoostMenu(j);
			}
		});
		for(int i = 1; i < 4;i ++)
		{
			final String mniejszygrubasek = (i == 1 ? "Speed I na 5 sekund" : (i == 2 ? "Speed I na 10 sekund" : "Speed I na 20 sekund"));
			goldenapple.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Ulepszenie " + ImportantColor + i + "/3" + SpecialSigns + " >> " + ImportantColor + Shops.getBoosts().get("MNIEJSZYGRUBASEK_" + i) + "$")));
			goldenapple.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + MainColor + "Po zjedzeniu refilia dostaniesz: ")));
			goldenapple.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    >> " + ImportantColor + BOLD  + mniejszygrubasek)));
			goldenapple.addLore("");
		}
		goldenapple.addLore("");
		goldenapple.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Postep: " + ImportantColor + u.getBoostRef() + "/3")));
		inv.setItem(5, goldenapple.build(), (j, arg1, arg2, arg3) -> {
			if(u.getBoostRef() >= 3){
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Masz juz wykupione to ulepszenie " + SpecialSigns + "(" + WarningColor_2 + "3/3" + SpecialSigns + ")")));
				return;
			}
			final int cost = Shops.getBoosts().get("MNIEJSZYGRUBASEK_" + (u.getBoostRef() + 1));
			if(u.getMoney() < cost)
				Util.sendMsg(j, Util.fixColor(Util.replaceString(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz " + SpecialSigns + "(" + WarningColor_2 + cost + "$" + SpecialSigns + ")")));
			else{
				u.removeMoneyViaPacket(cost);
				u.addBoostRefViaPacket(1);
				Util.sendMsg(j, Util.fixColor(Util.replaceString(SpecialSigns + ">>" + MainColor + " Zakupiles ulepszenie " + ImportantColor + "Mniejszy Grubasek" + SpecialSigns + " * " + ImportantColor + u.getBoostRef())));
				j.closeInventory();
				openBoostMenu(j);
			}
		});
		inv.setItem(size - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMainMenu(p);
		});
		inv.setItem(size - 2, money.build(), null);
		player.openInventory(inv.get());
	}
	private static void openTradeMenu(Player player)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(ShopConfig.tradeguiname)), ShopConfig.tradeguirows);
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		int position = 0;
		for(final TradeItems tradeitem : ShopConfig.tradestorage.values()){
			//if((tradeitem.getItem().equals(Material.DIAMOND_CHESTPLATE) || tradeitem.getItem().equals(Material.DIAMOND_HELMET) || tradeitem.getItem().equals(Material.DIAMOND_LEGGINGS) || tradeitem.getItem().equals(Material.DIAMOND_BOOTS) || tradeitem.getItem().equals(Material.DIAMOND_SWORD)) && !(CoreConfig.CREATEMANAGER_DIAMOND_TIME > System.currentTimeMillis()))
				inv.setItem(position, tradeitem.getItem_ItemBuilder().build(), (p, arg1, arg2, arg3) -> {
					final ItemStack itemStack = Core.getSpecialItemsManager().getServerVault(tradeitem.getCost_amount());
					if(!Core.getSpecialItemsManager().checkAndRemove(p,itemStack)){
						Util.sendMsg(p, Util.replaceString(ShopConfig.trademessage_enoughtitem
								.replace("{AMOUNT}", String.valueOf(tradeitem.getCost_amount()))
								.replace("{ITEM}", "JustCoin")));
						p.updateInventory();
					}else{
						ItemUtil.giveItems(Collections.singletonList(tradeitem.getItem_ItemStack()), p);
						Util.sendMsg(p, ShopConfig.trademessage_finaltrade);
						p.updateInventory();
					}

				});
				position++;
			}
			inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
				p.closeInventory();
				openMainMenu(p);
			});
			inv.openInventory(player);
		}
	}
