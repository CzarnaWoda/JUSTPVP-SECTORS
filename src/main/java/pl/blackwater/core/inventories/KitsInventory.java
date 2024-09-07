package pl.blackwater.core.inventories;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.EnchantManager;
import pl.blackwater.core.managers.ItemManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.KitsConfig;
import pl.blackwater.core.utils.KitsUtil;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

import java.util.List;

public class KitsInventory implements Colors
{
	public static void openKitMenu(Player player)
	{
		//testowe bez cooldowna i bez permisji :P
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(),Util.fixColor(Util.replaceString(MainColor + "Wybierz" + ImportantColor + " zestaw")),3);
		final ItemBuilder cyan = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)9).setTitle(Util.fixColor("&c"));
		final ItemBuilder gray = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)7).setTitle(Util.fixColor("&c"));
		inv.setItem(0, cyan.build(), null);
		inv.setItem(1, cyan.build(), null);
		inv.setItem(2, cyan.build(), null);
		inv.setItem(4, cyan.build(), null);
		inv.setItem(6, cyan.build(), null);
		inv.setItem(7, cyan.build(), null);
		inv.setItem(19, cyan.build(), null);
		inv.setItem(20, cyan.build(), null);
		inv.setItem(22, cyan.build(), null);
		inv.setItem(24, cyan.build(), null);
		inv.setItem(25, cyan.build(), null);
		inv.setItem(18, cyan.build(), null);
		inv.setItem(3, cyan.build(), null);
		inv.setItem(21, cyan.build(), null);
		inv.setItem(5, cyan.build(), null);
		inv.setItem(23, cyan.build(), null);
		inv.setItem(8, cyan.build(), null);
		inv.setItem(26, cyan.build(), null);
		inv.setItem(10, gray.build(), null);
		inv.setItem(11, gray.build(), null);
		inv.setItem(12, gray.build(), null);
		inv.setItem(14, gray.build(), null);
		inv.setItem(15, gray.build(), null);
		inv.setItem(16, gray.build(), null);
		final User u = UserManager.getUser(player);
		final ItemBuilder jedzenie = new ItemBuilder(Material.COOKED_BEEF).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zestaw: " + ImportantColor + BOLD + "MIESO")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zestaw zawiera: ")))
					.addLore("");
		final List<ItemStack> jedzenieitems = KitsUtil.getKitItems(KitsConfig.KIT_JEDZENIEITEMS);
		final int kjtime = (int) (u.getKitJEDZENIETime() + CoreConfig.KIT_JEDZENIE_TIME - System.currentTimeMillis());
		for(ItemStack item : jedzenieitems){
			jedzenie.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + ItemManager.get(item.getType()) + SpecialSigns + " >> " + MainColor + "x" + ImportantColor + item.getAmount() + (item.getEnchantments().size() > 0 ? SpecialSigns + " * " + ImportantColor + UnderLined + EnchantManager.getEnchantsLevel(item.getEnchantments()) : ""))));
		}
		jedzenie.addLore("");
		jedzenie.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "      >> " + MainColor + "Dostepnosc czasowa: " + (u.getKitJEDZENIETime() + CoreConfig.KIT_JEDZENIE_TIME > System.currentTimeMillis() ? ChatColor.DARK_RED + "%X% " + SpecialSigns + "(" + ChatColor.RED + KitsUtil.getDurationBreakdown(kjtime) + SpecialSigns + ")" : ChatColor.GREEN + "%V%"))));
		jedzenie.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "      >> " + MainColor + "Dostepnosc rangi: " + (player.hasPermission("core.kit.jedzenie") ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		inv.setItem(9, jedzenie.build(), (p, arg1, arg2, arg3) -> {
			if(!CoreConfig.KIT_JEDZENIE_STATUS){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Zestaw jest aktualnie globalnie wylaczony !"));
				return;
			}
			if(!p.hasPermission("core.kit.jedzenie")){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien do tego zestawu !"));
				return;
			}
			if(u.getKitJEDZENIETime() + CoreConfig.KIT_JEDZENIE_TIME > System.currentTimeMillis()){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Ten zestaw bedziesz mogl odebrac dopiero za " + KitsUtil.getDurationBreakdown(kjtime)));
				return;
			}
			p.closeInventory();
			final InventoryGUI kit = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(MainColor + "Zestaw" + ImportantColor + " MIESO")), 5);
			int position = 0;
			for(ItemStack item : jedzenieitems)
			{
				int amount = item.getAmount();
				if(item.getAmount() < 65){
					kit.setItem(position, item, null);
					position++;
				}else{
					final int items = amount/64;
					for(int i = 0; i < items;i++)
					{
						kit.setItem(position, item, null);
						amount = amount - 64;
						position ++;
					}
				}
			}
			final ItemBuilder get = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(MainColor + "Odbierz" + ImportantColor + BOLD + " zestaw")));
			kit.setItem(44, get.build(), (p14, arg114, arg214, arg314) -> {
				if(!p14.hasPermission("core.kit.admin"))
					u.setKitJEDZENIETime(System.currentTimeMillis());
				ItemUtil.giveItems(jedzenieitems, p14);
				p14.closeInventory();
				ActionBarUtil.sendActionBar(p14, Util.fixColor(Util.replaceString(ChatColor.GREEN + "%V%" + MainColor + " Odebrales zestaw" + ImportantColor + " MIESO " + ChatColor.GREEN + "%V%")));
			});
			kit.openInventory(p);

		});
		
		final ItemBuilder vip = new ItemBuilder(Material.IRON_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zestaw: " + ImportantColor + BOLD + "VIP")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zestaw zawiera: ")))
					.addLore("");
		final List<ItemStack> vipitems = KitsUtil.getKitItems(KitsConfig.KIT_VIPITEMS);
		final int kvtime = (int) (u.getKitVIPTime() + CoreConfig.KIT_VIP_TIME - System.currentTimeMillis());
		for(ItemStack item : vipitems){
			vip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + ItemManager.get(item.getType()) + SpecialSigns + " >> " + MainColor + "x" + ImportantColor + item.getAmount() + (item.getEnchantments().size() > 0 ? SpecialSigns + " * " + ImportantColor + UnderLined + EnchantManager.getEnchantsLevel(item.getEnchantments()) : ""))));
		}
		vip.addLore("");
		vip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "      >> " + MainColor + "Dostepnosc czasowa: " + (u.getKitVIPTime() + CoreConfig.KIT_VIP_TIME > System.currentTimeMillis() ? ChatColor.DARK_RED + "%X% " + SpecialSigns + "(" + ChatColor.RED + KitsUtil.getDurationBreakdown(kvtime) + SpecialSigns + ")" : ChatColor.GREEN + "%V%"))));
		vip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "      >> " + MainColor + "Dostepnosc rangi: " + (player.hasPermission("core.kit.vip") ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		inv.setItem(13, vip.build(), (p, arg1, arg2, arg3) -> {
			if(!CoreConfig.KIT_VIP_STATUS){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Zestaw jest aktualnie globalnie wylaczony !"));
				return;
			}
			if(!p.hasPermission("core.kit.vip")){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien do tego zestawu !"));
				return;
			}
			if(u.getKitVIPTime() + CoreConfig.KIT_VIP_TIME > System.currentTimeMillis()){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Ten zestaw bedziesz mogl odebrac dopiero za " + KitsUtil.getDurationBreakdown(kvtime)));
				return;
			}
			p.closeInventory();
			final InventoryGUI kit = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(MainColor + "Zestaw" + ImportantColor + " VIP")), 5);
			int position = 0;
			for(ItemStack item : vipitems)
			{
				int amount = item.getAmount();
				if(item.getAmount() < 65){
					kit.setItem(position, item, null);
					position++;
				}else{
					final int items = amount/64;
					for(int i = 0; i < items;i++)
					{
						kit.setItem(position, item, null);
						amount = amount - 64;
						position ++;
					}
				}
			}
			final ItemBuilder get = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(MainColor + "Odbierz" + ImportantColor + BOLD + " zestaw")));
			kit.setItem(44, get.build(), (p1, arg11, arg21, arg31) -> {
				if(!p1.hasPermission("core.kit.admin"))
					u.setKitVIPTime(System.currentTimeMillis());
				ItemUtil.giveItems(vipitems, p1);
				p1.closeInventory();
				ActionBarUtil.sendActionBar(p1, Util.fixColor(Util.replaceString(ChatColor.GREEN + "%V%" + MainColor + " Odebrales zestaw" + ImportantColor + " VIP " + ChatColor.GREEN + "%V%")));
			});
			kit.openInventory(p);

		});
		final ItemBuilder svip = new ItemBuilder(Material.GOLD_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zestaw: " + ImportantColor + BOLD + "SVIP")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zestaw zawiera: ")))
					.addLore("");
		final List<ItemStack> svipitems = KitsUtil.getKitItems(KitsConfig.KIT_SVIPITEMS);
		final int ksvtime = (int) (u.getKitSVIPTime() + CoreConfig.KIT_SVIP_TIME - System.currentTimeMillis());
		for(ItemStack item : svipitems){
			svip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + ItemManager.get(item.getType()) + SpecialSigns + " >> " + MainColor + "x" + ImportantColor + item.getAmount() + (item.getEnchantments().size() > 0 ? SpecialSigns + " * " + ImportantColor + UnderLined + EnchantManager.getEnchantsLevel(item.getEnchantments()) : ""))));
		}
		svip.addLore("");
		svip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "      >> " + MainColor + "Dostepnosc czasowa: " + (u.getKitSVIPTime() + CoreConfig.KIT_SVIP_TIME > System.currentTimeMillis() ? ChatColor.DARK_RED + "%X% " + SpecialSigns + "(" + ChatColor.RED + KitsUtil.getDurationBreakdown(ksvtime) + SpecialSigns + ")" : ChatColor.GREEN + "%V%"))));
		svip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "      >> " + MainColor + "Dostepnosc rangi: " + (player.hasPermission("core.kit.svip") ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		inv.setItem(17, svip.build(), (Player p, Inventory arg1, int arg2, ItemStack arg3) -> {
			if(!CoreConfig.KIT_SVIP_STATUS){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Zestaw jest aktualnie globalnie wylaczony !"));
				return;
			}
			if(!p.hasPermission("core.kit.svip")){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien do tego zestawu !"));
				return;
			}
			if(u.getKitSVIPTime() + CoreConfig.KIT_SVIP_TIME > System.currentTimeMillis()){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Ten zestaw bedziesz mogl odebrac dopiero za " + KitsUtil.getDurationBreakdown(ksvtime)));
				return;
			}
			p.closeInventory();
			final InventoryGUI kit = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(MainColor + "Zestaw" + ImportantColor + " SVIP")), 5);
			int position = 0;
			for(ItemStack item : svipitems)
			{
				int amount = item.getAmount();
				if(item.getAmount() < 65){
					kit.setItem(position, item, null);
					position++;
				}else{
					final int items = amount/64;
					for(int i = 0; i < items;i++)
					{
						kit.setItem(position, item, null);
						amount = amount - 64;
						position ++;
					}
				}
			}
			final ItemBuilder get = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(MainColor + "Odbierz" + ImportantColor + BOLD + " zestaw")));
			kit.setItem(44, get.build(), (p12, arg112, arg212, arg312) -> {
				if(!p12.hasPermission("core.kit.admin"))
					u.setKitSVIPTime(System.currentTimeMillis());
				ItemUtil.giveItems(svipitems, p12);
				p12.closeInventory();
				ActionBarUtil.sendActionBar(p12, Util.fixColor(Util.replaceString(ChatColor.GREEN + "%V%" + MainColor + " Odebrales zestaw" + ImportantColor + " SVIP " + ChatColor.GREEN + "%V%")));
			});
			kit.openInventory(p);

		});
		/*
				final ItemBuilder mvip = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Zestaw: " + ImportantColor + BOLD + "MVIP")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zestaw zawiera: ")))
					.addLore("");
		final List<ItemStack> mvipitems = KitsUtil.getKitItems(KitsConfig.KIT_MVIPITEMS);
		final int kmvtime = (int) (u.getKitMVIPTime() + CoreConfig.KIT_MVIP_TIME - System.currentTimeMillis());
		for(ItemStack item : mvipitems){
			mvip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + ItemManager.get(item.getType()) + SpecialSigns + " >> " + MainColor + "x" + ImportantColor + item.getAmount() + (item.getEnchantments().size() > 0 ? SpecialSigns + " * " + ImportantColor + UnderLined + EnchantManager.getEnchantsLevel(item.getEnchantments()) : ""))));
		}
		mvip.addLore("");
		mvip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "      >> " + MainColor + "Dostepnosc czasowa: " + (u.getKitMVIPTime() + CoreConfig.KIT_MVIP_TIME > System.currentTimeMillis() ? ChatColor.DARK_RED + "%X% " + SpecialSigns + "(" + ChatColor.RED + KitsUtil.getDurationBreakdown(kmvtime) + SpecialSigns + ")" : ChatColor.GREEN + "%V%"))));
		mvip.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "      >> " + MainColor + "Dostepnosc rangi: " + (player.hasPermission("core.kit.mvip") ? ChatColor.GREEN + "%V%" : ChatColor.DARK_RED + "%X%"))));
		inv.setItem(17, mvip.build(), (p, arg1, arg2, arg3) -> {
			if(!CoreConfig.KIT_MVIP_STATUS){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Zestaw jest aktualnie globalnie wylaczony !"));
				return;
			}
			if(!p.hasPermission("core.kit.mvip")){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien do tego zestawu !"));
				return;
			}
			if(u.getKitMVIPTime() + CoreConfig.KIT_MVIP_TIME > System.currentTimeMillis()){
				Util.sendMsg(p, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Ten zestaw bedziesz mogl odebrac dopiero za " + KitsUtil.getDurationBreakdown(kmvtime)));
				return;
			}
			p.closeInventory();
			final InventoryGUI kit = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(MainColor + "Zestaw" + ImportantColor + " MVIP")), 5);
			int position = 0;
			for(ItemStack item : mvipitems)
			{
				int amount = item.getAmount();
				if(item.getAmount() < 65){
					kit.setItem(position, item, null);
					position++;
				}else{
					final int items = amount/64;
					for(int i = 0; i < items;i++)
					{
						kit.setItem(position, item, null);
						amount = amount - 64;
						position ++;
					}
				}
			}
			final ItemBuilder get = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(MainColor + "Odbierz" + ImportantColor + BOLD + " zestaw")));
			kit.setItem(44, get.build(), (p13, arg113, arg213, arg313) -> {
				if(!p13.hasPermission("core.kit.admin"))
					u.setKitMVIPTime(System.currentTimeMillis());
				ItemUtil.giveItems(mvipitems, p13);
				p13.closeInventory();
				ActionBarUtil.sendActionBar(p13, Util.fixColor(Util.replaceString(ChatColor.GREEN + "%V%" + MainColor + " Odebrales zestaw" + ImportantColor + " MVIP " + ChatColor.GREEN + "%V%")));
			});
			kit.openInventory(p);

		});
		 */
		player.openInventory(inv.get());
	}

}
