package pl.blackwater.market.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.market.data.Auction;
import pl.blackwater.market.managers.AuctionManager;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;


public class MarketUtil {
	public static InventoryView openMainMenu(Player p ){
		Inventory inv = Bukkit.createInventory(null, 27,Util.fixColor(Util.replaceString("&2%V% &cMarket &2%V%")));
		ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)15).setTitle(Util.fixColor("&2"));
		for(int i = 0; i < inv.getSize(); i ++){
			inv.setItem(i, air.build());
		}
		ItemBuilder createauction = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString("&8>> &6Wystaw przedmiot na &7market")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &6Kliknij &7LPM &6aby wystawic przedmiot na &7market")));
		inv.setItem(10, createauction.build());
		ItemBuilder auctionlist = new ItemBuilder(Material.BOOK_AND_QUILL).setTitle(Util.fixColor(Util.replaceString("&8>> &6Lista Aukcjii")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &6Kliknij aby przejsc do &7listy aukcjii &6!")));
		inv.setItem(16, auctionlist.build());
		return p.openInventory(inv);
		//16
	}
	public static InventoryView OpenMenu_First(Player p){
		Inventory inv = Bukkit.createInventory(null, 54,Util.fixColor(Util.replaceString("&2%V% &cMarket &8(&41&6/&42&8) &2%V%")));
		int position = 0;
		ItemStack[] contents = p.getInventory().getContents();
		for(ItemStack item : contents){
			inv.setItem(position, item);
			position ++;
		}
		return p.openInventory(inv);
	}
	public static InventoryView OpenMenu_Second(Player p,ItemStack item,int cost){
		Inventory inv = Bukkit.createInventory(null, 36,Util.fixColor(Util.replaceString("&2%V% &cMarket &8(&42&6/&42&8) &2%V%")));
		ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)15).setTitle(Util.fixColor("&2"));
		for(int i = 0; i < inv.getSize(); i ++){
			inv.setItem(i, air.build());
		}
		ItemStack itemStack = item;
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(Util.fixColor(Util.replaceString("&6Market &8&l>> &7" + ItemUtil.getPolishMaterial(itemStack.getType()) + " &8&l<<")));
		List<String> lore = Arrays.asList(Util.fixColor(Util.replaceString("&8&l* &6Cena przedmiotu: &7" + cost)));
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		inv.setItem(13, itemStack);
		//20
		ItemBuilder eb = new ItemBuilder(Material.EMERALD_BLOCK).setTitle(Util.fixColor(Util.replaceString("&8&l>> &2Wystaw przedmiot na &a&lmarket &8&l<<")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &6Kliknij &7LPM &6aby wystawic przedmiot na market")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &6Zaliczka za wystawienie wynosi: &7350$")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &6Zaliczka zostanie zwrocona, gdy &7przedmiot &6zostanie &7kupiony &6!")));
		inv.setItem(20, eb.build());
		ItemBuilder rb = new ItemBuilder(Material.REDSTONE_BLOCK).setTitle(Util.fixColor(Util.replaceString("&8&l>> &cAnuluj wystawianie przedmiotu na &4&lmarket &8&l<<")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &6Kliknij &7LPM &6aby anulowac wystawienie przedmiotu na market")));
		inv.setItem(24, rb.build());
		return p.openInventory(inv);
	}
	public static InventoryView OpenMenu_AuctionList(Player p){
		Inventory inv = Bukkit.createInventory(null, 54,Util.fixColor(Util.replaceString("&8&l>> &c&lLista Aukcjii &8&l<<")));
		ItemBuilder arrow = new ItemBuilder(Material.ARROW).setTitle(Util.fixColor(Util.replaceString("&8&l>> &2Nastepna Strona")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &61-52")));
		Set<Auction> set = AuctionManager.getFromToAuction(1, 53);
		int i = 0;
		for(Auction a : set){
			ItemStack item = a.getItem();
			ItemMeta meta = item.getItemMeta();
	        SimpleDateFormat d = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	        Date date = new Date(a.getDate());
	        String show = d.format(date);
			meta.setDisplayName(Util.fixColor(Util.replaceString("&2&lMarket &8&l* &a" + ItemUtil.getPolishMaterial(item.getType()) + " &8&l*")));
			final User u = UserManager.getUser(a.getOwner());
			meta.setLore(Arrays.asList(Util.fixColor(Util.replaceString("    &8&l>> &2Cena przedmiotu: &7" + a.getCost()))
						,Util.fixColor(Util.replaceString("    &8&l>> &2Data wystawienia: &7" + show))
						,Util.fixColor(Util.replaceString("    &8&l>> &2Wlasciciel: &7" + (u == null ? "" : u.getLastName())))
						,Util.fixColor(Util.replaceString("    &8&l>> &2ID Aukcjii: &7" + a.getIndex()))));
			item.setItemMeta(meta);
			if(u != null) {
				inv.setItem(i, item);
			}
			else {
				Core.getPlugin().getLogger().log(Level.INFO, " USER NULL -> " + a.getOwner() + " dla takiego UUID!");
			}
			i++;
		}
		inv.setItem(53,arrow.build());
		return p.openInventory(inv);
	}
	public static InventoryView OpenMenu_FinalBuy(Player p, Auction a){
		Inventory inv = Bukkit.createInventory(null, 36,Util.fixColor(Util.replaceString("&2%V% &cAuction &2%V%")));
		ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)15).setTitle(Util.fixColor("&2"));
		for(int i = 0; i < inv.getSize(); i ++){
			inv.setItem(i, air.build());
		}
		ItemBuilder eb = new ItemBuilder(Material.EMERALD_BLOCK).setTitle(Util.fixColor(Util.replaceString("&8&l>> &2Kup przedmiot z &a&lmarketu &8&l<<")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &6Kliknij &7LPM &6aby zakupic przedmiot")));
		inv.setItem(20, eb.build());
		ItemBuilder rb = new ItemBuilder(Material.REDSTONE_BLOCK).setTitle(Util.fixColor(Util.replaceString("&8&l>> &cAnuluj kupno &4&lprzedmiotu &8&l<<")))
					.addLore(Util.fixColor(Util.replaceString("    &8&l* &6Kliknij &7LPM &6aby anulowac kupno przedmiotu z marketu")));
		inv.setItem(24, rb.build());
		ItemStack itemStack = a.getItem();
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(Util.fixColor(Util.replaceString("&6Market &8&l>> &7" + ItemUtil.getPolishMaterial(itemStack.getType()) + " &8&l<<")));
		List<String> lore = Arrays.asList(Util.fixColor(Util.replaceString("&8&l* &6Cena przedmiotu: &7" + a.getCost())),Util.fixColor(Util.replaceString("&8&l* &6ID Aukcjii: &7" + a.getIndex())));
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		inv.setItem(13, itemStack);
		return p.openInventory(inv);
	}

}
