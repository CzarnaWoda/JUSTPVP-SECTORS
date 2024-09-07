package pl.blackwater.core.inventories;

import net.lightshard.itemcases.ItemCases;
import net.lightshard.itemcases.cases.ItemCase;
import net.lightshard.itemcases.userinterface.preset.Uis;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.meta.ItemMeta;
import pl.blackwater.chestpvpdrop.data.Drop;
import pl.blackwater.chestpvpdrop.drops.RandomDropData;
import pl.blackwater.chestpvpdrop.managers.DropManager;
import pl.blackwater.chestpvpdrop.settings.Config;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.EventManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.utils.ColoredMaterialsUtil;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DropInventory implements Colors{
	
	public static InventoryView openMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Drop Menu" + SpecialSigns + " *")), 3);
		final User u = UserManager.getUser(player);
		final ItemBuilder players = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Drop z" + ChatColor.LIGHT_PURPLE + " GRACZY")));
		final ItemBuilder stone = new ItemBuilder(Material.STONE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Drop z" + ChatColor.BLUE + " STONE")));
		final ItemBuilder chest = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Drop z" + ChatColor.YELLOW + " SKRZYNEK")));
		final ItemBuilder notifi = new ItemBuilder(Material.BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Wylacz/Wlacz powiadomienia z" + ChatColor.WHITE + " STONE")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  *" + MainColor + " Status: " + (RandomDropData.isNoKomunikaty(player.getUniqueId()) ? ChatColor.DARK_RED + "%X%" : ChatColor.GREEN + "%V%"))));
		final ItemBuilder turbodrop = new ItemBuilder(Material.DIAMOND_PICKAXE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + UnderLined + "TurboDrop" + SpecialSigns + " <<")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Tryb " + WarningColor + "GRACZ" + SpecialSigns + " *")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (u.isTurboDrop() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (u.isTurboDrop() ? "Aktywny do: " + ImportantColor + Util.getDate(u.getTurboDropTime()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int) ((int)(u.getTurboDropTime() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (u.getTurboDropTime() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(u.getTurboDropTime()))))))
				.addLore(Util.fixColor(""))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Tryb " + WarningColor + "ALL" + SpecialSigns + " *")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (EventManager.isOnTurboDrop() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (EventManager.isOnTurboDrop() ? "Aktywny do: " + ImportantColor + Util.getDate(EventManager.getTurbodrop_time()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int)((int)(EventManager.getTurbodrop_time() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (EventManager.getTurbodrop_time() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(EventManager.getTurbodrop_time()))))));
		final ItemBuilder turboexp = new ItemBuilder(Material.EXP_BOTTLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + UnderLined + "TurboExp" + SpecialSigns + " <<")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Tryb " + WarningColor + "GRACZ" + SpecialSigns + " *")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (u.isTurboExp() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (u.isTurboExp() ? "Aktywny do: " + ImportantColor + Util.getDate(u.getTurboExpTime()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int) ((int)(u.getTurboExpTime() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (u.getTurboExpTime() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(u.getTurboExpTime()))))))
				.addLore(Util.fixColor(""))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Tryb " + WarningColor + "ALL" + SpecialSigns + " *")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (EventManager.isOnTurboExp() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (EventManager.isOnTurboExp() ? "Aktywny do: " + ImportantColor + Util.getDate(EventManager.getTurboexp_time()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int)((int)(EventManager.getTurboexp_time() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (EventManager.getTurboexp_time() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(EventManager.getTurboexp_time()))))));
		final ItemBuilder turbomoney = new ItemBuilder(Material.GOLD_INGOT).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + UnderLined + "TurboMoney" + SpecialSigns + " <<")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Tryb " + WarningColor + "GRACZ" + SpecialSigns + " *")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (u.getEventTurboMoney() == 2 ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (u.getEventTurboMoney() == 2 ? "Aktywny do: " + ImportantColor + Util.getDate(u.getEventTurboMoneyTime()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int) ((int)(u.getEventTurboMoneyTime() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (u.getEventTurboMoneyTime() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(u.getEventTurboMoneyTime()))))))
				.addLore(Util.fixColor(""))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Tryb " + WarningColor + "ALL" + SpecialSigns + " *")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (EventManager.isOnTurboMoney() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (EventManager.isOnTurboMoney() ? "Aktywny do: " + ImportantColor + Util.getDate(EventManager.getTurbomoney_time()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int)((int)(EventManager.getTurbomoney_time() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (EventManager.getTurbomoney_time() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(EventManager.getTurbomoney_time()))))));
		final ItemBuilder events = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + UnderLined + "Eventy" + SpecialSigns + " <<")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Event: " + ChatColor.BLUE + "DROP")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (EventManager.isOnEventDrop() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (EventManager.isOnEventDrop() ? "Aktywny do: " + ImportantColor + Util.getDate(EventManager.getDrop_time()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int)((int)(EventManager.getDrop_time() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (EventManager.getDrop_time() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(EventManager.getDrop_time()))))))
				.addLore(Util.fixColor(Util.replaceString((EventManager.isOnEventDrop() ? SpecialSigns + "  * " + MainColor + "Mnoznik: x" + ImportantColor + UnderLined + Config.EVENT_DROP : ""))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Event: " + ChatColor.BLUE + "EXP")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (EventManager.isOnEventExp() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (EventManager.isOnEventExp() ? "Aktywny do: " + ImportantColor + Util.getDate(EventManager.getExp_time()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int)((int)(EventManager.getExp_time() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (EventManager.getExp_time() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(EventManager.getExp_time()))))))
				.addLore(Util.fixColor(Util.replaceString((EventManager.isOnEventExp() ? SpecialSigns + "  * " + MainColor + "Mnoznik: x" + ImportantColor + UnderLined + Config.EVENT_EXP : ""))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Event: " + ChatColor.BLUE + "INFINITYSTONE")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktywny: " + (EventManager.isOnEventInfinityStone() ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + (EventManager.isOnEventInfinityStone() ? "Aktywny do: " + ImportantColor + Util.getDate(EventManager.getInfinitystone_time()) + SpecialSigns + " (" + WarningColor + Util.secondsToString((int)((int)(EventManager.getInfinitystone_time() - System.currentTimeMillis()) / 1000L)) + SpecialSigns + ")" : "Ostatnio aktywny: " + (EventManager.getInfinitystone_time() == 0L ? ChatColor.DARK_RED + "Nigdy" : ImportantColor + Util.getDate(EventManager.getInfinitystone_time()))))))
				.addLore(Util.fixColor(Util.replaceString("&a")));
		final ItemBuilder purple = ColoredMaterialsUtil.getStainedGlassPane((short)10);
		final ItemBuilder lblue = ColoredMaterialsUtil.getStainedGlassPane((short)3);
		final ItemBuilder brown = ColoredMaterialsUtil.getStainedGlassPane((short)12);
		final ItemBuilder white = ColoredMaterialsUtil.getStainedGlassPane((short)0);
		final ItemBuilder black = ColoredMaterialsUtil.getStainedGlassPane((short)15);
		final ItemBuilder blue = ColoredMaterialsUtil.getStainedGlassPane((short)11);
		final ItemBuilder pink = ColoredMaterialsUtil.getStainedGlassPane((short)6);
		final ItemBuilder yellow = ColoredMaterialsUtil.getStainedGlassPane((short)4);
		final ItemBuilder red = ColoredMaterialsUtil.getStainedGlassPane((short)14);
		inv.setItem(0, purple.build(), null);
		inv.setItem(9, players.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenuPlayerDrop(p);
		});
		inv.setItem(18, purple.build(), null);
		inv.setItem(1, lblue.build(), null);
		inv.setItem(10, stone.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenuStoneDrop(p);
		});
		inv.setItem(19, lblue.build(), null);
		inv.setItem(2, brown.build(), null);
		inv.setItem(11, chest.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenuChestDrop(p);
		});
		inv.setItem(20, brown.build(), null);
		inv.setItem(3, white.build(), null);
		inv.setItem(12, notifi.build(), (p, arg1, arg2, item) -> {
			RandomDropData.changeNoKomunikaty(p.getUniqueId());
			ItemMeta meta = item.getItemMeta();
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "  *" + MainColor + " Status: " + (RandomDropData.isNoKomunikaty(p.getUniqueId()) ? ChatColor.DARK_RED + "%X%" : ChatColor.GREEN + "%V%")))));
			item.setItemMeta(meta);
		});
		inv.setItem(21, white.build(), null);
		inv.setItem(4, black.build(), null);
		inv.setItem(13, black.build(), null);
		inv.setItem(22, black.build(), null);
		inv.setItem(5, blue.build(), null);
		inv.setItem(14, turbodrop.build(), null);
		inv.setItem(23, blue.build(), null);
		inv.setItem(6, yellow.build(), null);
		inv.setItem(15, turbomoney.build(), null);
		inv.setItem(24, yellow.build(), null);
		inv.setItem(7, pink.build(), null);
		inv.setItem(16, turboexp.build(), null);
		inv.setItem(25, pink.build(), null);
		inv.setItem(8, red.build(), null);
		inv.setItem(17, events.build(), null);
		inv.setItem(26, red.build(), null);
		return player.openInventory(inv.get());
	}
	private static void openMenuPlayerDrop(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Drop Menu" + SpecialSigns + " *")), 1);
		final User user = UserManager.getUser(player);
		final int rankmoney = player.hasPermission("core.svip") ? CoreConfig.DROPMANAGER_MONEY_SVIP : (player.hasPermission("core.vip") ? CoreConfig.DROPMANAGER_MONEY_VIP : CoreConfig.DROPMANAGER_MONEY_DEFAULT);
		final int earnmoney = (int)(rankmoney + ((rankmoney * Math.min(1,user.getKillStreak()*0.004D))));
		final ItemBuilder money = new ItemBuilder(Material.GOLD_INGOT).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Drop" + ChatColor.GOLD + " Monet")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Za kazdego gracza: " + ImportantColor + earnmoney)));
		final int earnlevel = (player.hasPermission("core.svip") ? CoreConfig.DROPMANAGER_LEVEL_SVIP : (player.hasPermission("core.vip") ? CoreConfig.DROPMANAGER_LEVEL_VIP : CoreConfig.DROPMANAGER_LEVEL_DEFAULT));
		final ItemBuilder level = new ItemBuilder(Material.EXP_BOTTLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Drop" + ChatColor.LIGHT_PURPLE + " Level EXP")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Za kazdego gracza: " + ImportantColor + earnlevel)));
		final double keychance = (player.hasPermission("core.svip") ? CoreConfig.DROPMANAGER_KEY_CHANCE_SVIP : (player.hasPermission("core.vip") ? CoreConfig.DROPMANAGER_KEY_CHANCE_VIP : CoreConfig.DROPMANAGER_KEY_CHANCE_DEFAULT));
		final ItemBuilder key = new ItemBuilder(Material.TRIPWIRE_HOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Szansa na" + ChatColor.BLUE + " Klucz")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Szansa na wypadniecie z gracza: " + ImportantColor + keychance + MainColor + "%")));
		final int earndiamond = (player.hasPermission("core.svip") ? CoreConfig.DROPMANAGER_DIAMOND_SVIP : (player.hasPermission("core.vip") ? CoreConfig.DROPMANAGER_DIAMOND_VIP : CoreConfig.DROPMANAGER_DIAMOND_DEFAULT));
		final ItemBuilder diamond = new ItemBuilder(Material.DIAMOND).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Szansa na" + ChatColor.AQUA + " Diament")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Ilosc diamentow: " + ImportantColor + earndiamond)));
		final double min = CoreConfig.DROPMANAGER_EXP_MIN;
		final double max = CoreConfig.DROPMANAGER_EXP_MAX;
		final ItemBuilder exp = new ItemBuilder(Material.WOOD_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Drop" + ChatColor.GREEN + " Doswiadczenia")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Za kazdego gracza: " + ImportantColor + min + MainColor + " - " + ImportantColor + max)));
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		inv.setItem(0, money.build(), null);
		inv.setItem(1, level.build(), null);
		inv.setItem(2, key.build(), null);
		inv.setItem(3, diamond.build(), null);
		inv.setItem(4, exp.build(), null);
		inv.setItem(inv.get().getSize() - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenu(p);

		});
		player.openInventory(inv.get());
	}
	private static void openMenuStoneDrop(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Drop Menu" + SpecialSigns + " *")), 4);
		int position = 0;
		final User user = UserManager.getUser(player);
        for (Drop cbl : RandomDropData.getDrops()) {
        	ItemBuilder itemBuilder = new ItemBuilder(cbl.getWhat().getType(), 1).setTitle(Util.fixColor(ImportantColor + cbl.getName().toUpperCase())).addLores(Arrays.asList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Szansa na drop: " + ImportantColor + ((EventManager.isOnEventDrop() ? (cbl.getChance() + (cbl.getWhat().getType().equals(Material.ENDER_PEARL) ? 0 : user.getBonusDrop()))*Config.EVENT_DROP : cbl.getChance() + (cbl.getWhat().getType().equals(Material.ENDER_PEARL) ? 0 : user.getBonusDrop())) + "%") + (UserManager.getUser(player).isPremium() ? (MainColor + " + " + ImportantColor + cbl.getChanceVIP() + "% " + SpecialSigns + "(" + MainColor + "Premium" + SpecialSigns + ")") : "")) + (EventManager.isOnTurboDrop() || user.isTurboDrop() ? (MainColor + " + " + ImportantColor + cbl.getChanceTurboDrop() + "% " + SpecialSigns + "(" + MainColor + "TurboDrop" + SpecialSigns + ")") : "")), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Dzialanie zaklecia fortuny: " + (cbl.isFortune() ? ChatColor.GREEN + "Tak" : ChatColor.DARK_RED + "Nie"))), Util.fixColor(Util.replaceString(SpecialSigns + "* " +  MainColor + "Aktywny: " + (cbl.isDisabled(player.getUniqueId()) ? ChatColor.DARK_RED + "Nie" : ChatColor.GREEN + "Tak"))), "", Util.fixColor(Util.replaceString(SpecialSigns + ">>" +  MainColor + " Pamietaj! Ranga " +  ImportantColor + "VIP" + MainColor + " posiada wiekszy drop o " + ImportantColor + Config.DROP_VIP_DROP + "%" + MainColor + "!"))));
        	inv.setItem(position, itemBuilder.build(), (p, arg1, arg2, item) -> {
				final ItemMeta meta = item.getItemMeta();
				final Drop d = RandomDropData.getDropByName(ChatColor.stripColor(Util.fixColor(meta.getDisplayName())));
				if(d != null) {
					d.changeStatus(p.getUniqueId());
					final List<String> lore = meta.getLore();
					lore.set(2, SpecialSigns + "* " +  MainColor + "Aktywny: " + (d.isDisabled(p.getUniqueId()) ? ChatColor.DARK_RED + "Nie" : ChatColor.GREEN + "Tak"));
					meta.setLore(lore);
					item.setItemMeta(meta);
				}
			});
        	position++;
        }
        ItemBuilder exp = new ItemBuilder(Material.EXP_BOTTLE).setTitle(Util.fixColor(Util.replaceString(ImportantColor + "Doswiadczenie"))).addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Kamien: " + ImportantColor + (EventManager.isOnEventExp() ? DropManager.getExp(Material.STONE) * Config.EVENT_DROP : DropManager.getExp(Material.STONE)) + MainColor + (user.isPremium() ? " + " + ImportantColor + Config.DROP_VIP_EXP + SpecialSigns + " (" + MainColor + "PREMIUM" + SpecialSigns + ")" : "") + (user.isTurboExp() || EventManager.isOnTurboExp() ? MainColor + " + " + ImportantColor + Config.DROP_TURBO_EXP + SpecialSigns + " (" + MainColor + "TurboEXP" + SpecialSigns + ")" : ""))));
        ItemBuilder cb = new ItemBuilder(Material.COBBLESTONE).setTitle(Util.fixColor(ImportantColor + "Cobblestone")).addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Aktywny: " + (RandomDropData.isNoCobble(player.getUniqueId()) ? ChatColor.DARK_RED + "Nie" : ChatColor.GREEN + "Tak"))));
        ItemBuilder ch1 = new ItemBuilder(Material.ENCHANTED_BOOK).setTitle(Util.fixColor(ImportantColor + "Fortune I")).addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Szansa: " + ImportantColor + "10%")));
        ItemBuilder ch2 = new ItemBuilder(Material.ENCHANTED_BOOK).setTitle(Util.fixColor(ImportantColor + "Fortune II")).addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Szansa: " + ImportantColor + "20%")));
        ItemBuilder ch3 = new ItemBuilder(Material.ENCHANTED_BOOK).setTitle(Util.fixColor(ImportantColor + "Fortune III")).addLore(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Szansa: " + ImportantColor + "30%")));
        inv.setItem(27, cb.build(), (p, arg1, arg2, item) -> {
			RandomDropData.changeNoCobble(p.getUniqueId());
			final List<String> lore = Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Aktywny: " + (RandomDropData.isNoCobble(p.getUniqueId()) ? ChatColor.DARK_RED + "Nie" : ChatColor.GREEN + "Tak"))));
			final ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
		});
        final int size = inv.get().getSize();
        inv.setItem(size - 6, ch1.build(),null);
        inv.setItem(size - 5, ch2.build(),null);
        inv.setItem(size - 4, ch3.build(),null);
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.replaceString(SpecialSigns + "* " + WarningColor_2 + "Powrot" + SpecialSigns + " *"));
		inv.setItem(size - 1, back.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			openMenu(p);

		});
		inv.setItem(28,exp.build(),null);
		player.openInventory(inv.get());
	}
	private static void openMenuChestDrop(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Drop Menu" + SpecialSigns + " *")), 1);
		final ItemBuilder chestpvp = new ItemBuilder(Material.CHEST,1).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Drop z skrzynki " + ImportantColor + "ChestPvP")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Klucze sa dostepne na stronie: " + ImportantColor + "sklep.justpvp.pl")));
		final ItemBuilder justpvp = new ItemBuilder(Material.CHEST,2).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Drop z skrzynki " + ImportantColor + "JustPvP")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Klucze sa dostepne na stronie: " + ImportantColor + "sklep.justpvp.pl")));
		final ItemBuilder minechest = new ItemBuilder(Material.CHEST,3).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Drop z skrzynki " + ImportantColor + "MineChest")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Klucze sa dostepne na stronie: " + ImportantColor + "sklep.justpvp.pl")));
		final ItemBuilder startpack = new ItemBuilder(Material.CHEST,4).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Drop z skrzynki " + ImportantColor + "StartPack")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Klucze sa dostepne na stronie: " + ImportantColor + "sklep.justpvp.pl")));
		inv.setItem(0, chestpvp.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			ItemCase itemCase = ItemCases.getInstance().getCaseManager().fromName("chestpvp");
			if(itemCase != null) {
				Uis.get(p).setCurrentlyPreviewing(itemCase);
				Uis.get(p).setPage(1);
				Uis.REWARDS_PREVIEW.show(p);
			}

		});
		inv.setItem(1, justpvp.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			ItemCase itemCase = ItemCases.getInstance().getCaseManager().fromName("justpvp");
			if(itemCase != null) {
				Uis.get(p).setCurrentlyPreviewing(itemCase);
				Uis.get(p).setPage(1);
				Uis.REWARDS_PREVIEW.show(p);
			}

		});
		inv.setItem(2, minechest.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			ItemCase itemCase = ItemCases.getInstance().getCaseManager().fromName("minechest");
			if(itemCase != null) {
				Uis.get(p).setCurrentlyPreviewing(itemCase);
				Uis.get(p).setPage(1);
				Uis.REWARDS_PREVIEW.show(p);
			}

		});
		inv.setItem(3, startpack.build(), (p, arg1, arg2, arg3) -> {
			p.closeInventory();
			ItemCase itemCase = ItemCases.getInstance().getCaseManager().fromName("startpack");
			if(itemCase != null) {
				Uis.get(p).setCurrentlyPreviewing(itemCase);
				Uis.get(p).setPage(1);
				Uis.REWARDS_PREVIEW.show(p);
			}

		});
		player.openInventory(inv.get());
	}
	

}
