package pl.blackwater.market.listeners;

import java.text.SimpleDateFormat;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.ChatControlUserManager;
import pl.blackwater.core.managers.ItemManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.market.data.Auction;
import pl.blackwater.market.managers.AuctionManager;
import pl.blackwater.market.utils.MarketUtil;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.auctions.AuctionRegisterPacket;
import pl.justsectors.packets.impl.auctions.AuctionSoldPacket;
import pl.justsectors.redis.client.RedisClient;


public class AuctionListener implements Listener{
	private Map<Player,ItemStack> temporarystorage = new HashMap<>();
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(e.getInventory().getName().equalsIgnoreCase(Util.fixColor(Util.replaceString("&2%V% &cMarket &8(&41&6/&42&8) &2%V%")))){
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if (item != null) {
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {
					Player p = (Player)e.getWhoClicked();
					p.closeInventory();
					if(item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == 0 && item.getAmount() > CoreConfig.LIMITMANAGER_REF || item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == 1 && item.getAmount() > CoreConfig.LIMITMANAGER_KOX || item.getType().equals(Material.ENDER_PEARL) && item.getDurability() == 0 && item.getAmount() > CoreConfig.LIMITMANAGER_PEARL){
						TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4Blad !")), Util.fixColor(Util.replaceString("&8>> &6Blad z zweryfikowaniem przedmiotu, &7sprobuj ponownie&6!")));
						return;
					}
					if(item.getType().equals(Material.ENCHANTED_BOOK) || item.getType().equals(Material.TRIPWIRE_HOOK) || item.getType().equals(Material.ARROW)){
						p.closeInventory();
						TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&4Blad: &7Tego przedmiotu nie mozna wystawic na aukcje !")));
						if(temporarystorage.get(p) != null){
							temporarystorage.remove(p);
						}
						return;
					}
					TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&8&l>> &6Wpisz cene &7przedmiotu &6na chacie &8&l<<")));
					if(temporarystorage.get(p) == null){
						temporarystorage.put(p, item);
					}else{
						temporarystorage.remove(p);
						temporarystorage.put(p, item);
					}
					ItemUtil.removeItems(Arrays.asList(item), p);
				}
			}

		}else if(e.getInventory().getName().equalsIgnoreCase(Util.fixColor(Util.replaceString("&2%V% &cMarket &8(&42&6/&42&8) &2%V%")))){
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if (item != null) {
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {
					if (meta.getDisplayName() != null && meta.getDisplayName().equals(Util.fixColor(Util.replaceString("&8&l>> &2Wystaw przedmiot na &a&lmarket &8&l<<")))) {
						Player p = (Player)e.getWhoClicked();
						User u = UserManager.getUser(p);
						if(u.getMoney() >= 350){
							ItemStack itemStack = e.getInventory().getItem(13);
							String s = itemStack.getItemMeta().getLore().get(0);
							int cost = Integer.valueOf(s.replace(Util.fixColor(Util.replaceString("&8&l* &6Cena przedmiotu: &7")), ""));
							Auction a = AuctionManager.createAuction(p, cost,itemStack);
							a.insert();
							RedisClient.sendSectorsPacket(new AuctionRegisterPacket(a, u.getLastName()));
							p.closeInventory();
							temporarystorage.remove(p);
						}else{
							p.closeInventory();
							TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&cNie posiadasz 350$ na zaliczke !")));
							ItemStack is = temporarystorage.get(p);
							ItemMeta ism = is.getItemMeta();
							ism.setDisplayName(null);
							ism.setLore(null);
							is.setItemMeta(ism);
							p.getInventory().addItem(is);
							if(temporarystorage.get(p) != null){
								temporarystorage.remove(p);
							}
							return;
						}
					}
					if (meta.getDisplayName() != null && meta.getDisplayName().equals(Util.fixColor(Util.replaceString("&8&l>> &cAnuluj wystawianie przedmiotu na &4&lmarket &8&l<<")))) {
						Player p = (Player)e.getWhoClicked();
						ItemStack is = temporarystorage.get(p);
						ItemMeta ism = is.getItemMeta();
						ism.setDisplayName(null);
						ism.setLore(null);
						is.setItemMeta(ism);
						p.getInventory().addItem(is);
						if(temporarystorage.get(p) != null){
							temporarystorage.remove(p);
						}
						p.closeInventory();
						TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&cAnulowano wystawienie przedmiotu na market !")));

					}
				}
			}
		}else if(e.getInventory().getName().equalsIgnoreCase(Util.fixColor(Util.replaceString("&2%V% &cMarket &2%V%")))){
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if (item != null) {
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {
					if(meta.getDisplayName() != null && meta.getDisplayName().equals(Util.fixColor(Util.replaceString("&8>> &6Wystaw przedmiot na &7market")))){
						Player p = (Player)e.getWhoClicked();
						p.closeInventory();
						MarketUtil.OpenMenu_First(p);
					}
					if(meta.getDisplayName() != null && meta.getDisplayName().equals(Util.fixColor(Util.replaceString("&8>> &6Lista Aukcjii")))){
						Player p = (Player)e.getWhoClicked();
						p.closeInventory();
						MarketUtil.OpenMenu_AuctionList(p);
					}

				}
			}
		}else if(e.getInventory().getName().equalsIgnoreCase(Util.fixColor(Util.replaceString("&8&l>> &c&lLista Aukcjii &8&l<<")))){
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if (item != null) {
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {
					if(meta.getDisplayName() != null && meta.getDisplayName().equals(Util.fixColor(Util.replaceString("&8&l>> &2Nastepna Strona")))){
						String s = meta.getLore().get(0).replace(Util.fixColor(Util.replaceString("    &8&l* &6")), "");
						String[] range = s.split("-");
						int to = Integer.valueOf(range[1]);
						int auctionsamount = AuctionManager.getNonSoldAuctions().size();
						Inventory inv = e.getInventory();
						if(auctionsamount > to){
							int newfrom = to + 1;
							int newto = newfrom + 52;
							for(int i  = 0 ; i < 53; i ++){
								inv.setItem(i, null);
							}
							Set<Auction> set = AuctionManager.getFromToAuction(newfrom, newto);
							int i = 0;
							for(Auction a : set){
								ItemStack aitem = a.getItem();
								ItemMeta ameta = aitem.getItemMeta();
								SimpleDateFormat d = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
								Date date = new Date(a.getDate());
								String show = d.format(date);
								ameta.setDisplayName(Util.fixColor(Util.replaceString("&2&lMarket &8&l* &a" + ItemManager.get(aitem.getType()) + " &8&l*")));
								ameta.setLore(Arrays.asList(Util.fixColor(Util.replaceString("    &8&l>> &2Cena przedmiotu: &7" + a.getCost()))
										,Util.fixColor(Util.replaceString("    &8&l>> &2Wlasciciel: &7" + (UserManager.getUser(a.getOwner()) == null ? "" : UserManager.getUser(a.getOwner()).getLastName())))
										,Util.fixColor(Util.replaceString("    &8&l>> &2Data wystawienia: &7" + show))
										,Util.fixColor(Util.replaceString("    &8&l>> &2ID Aukcjii: &7" + a.getIndex()))));
								aitem.setItemMeta(ameta);
								inv.setItem(i, aitem);
								i++;
							}
							meta.setLore(Arrays.asList(Util.fixColor(Util.replaceString("    &8&l* &6" + newfrom + "-" + newto))));
							item.setItemMeta(meta);
							ItemBuilder back = new ItemBuilder(Material.ARROW,1).setTitle(Util.fixColor(Util.replaceString("&8&l>> &cPoprzednia Strona")))
									.addLore(Util.fixColor(Util.replaceString("    &8&l* &6" + newfrom + "-" + newto)));
							inv.setItem(inv.getSize()-2, back.build());
						}else{
							Player p = (Player)e.getWhoClicked();
							p.closeInventory();
							TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&cBrak wiekszej ilosci aukcjii !")));
						}
					}else if(meta.getDisplayName() != null && meta.getDisplayName().equals(Util.fixColor(Util.replaceString("&8&l>> &cPoprzednia Strona")))){
						String s = meta.getLore().get(0).replace(Util.fixColor(Util.replaceString("    &8&l* &6")), "");
						String[] range = s.split("-");
						int from = Integer.parseInt(range[0]);
						int to = Integer.parseInt(range[1]);
						//53 - 105
						int auctionsamount = AuctionManager.getNonSoldAuctions().size();
						Inventory inv = e.getInventory();
						if(auctionsamount > (to - 52)){
							int newfrom = from - 52;
							int newto = to - 52;
							for(int i  = 0 ; i < 53; i ++){
								inv.setItem(i, null);
							}
							Set<Auction> set = AuctionManager.getFromToAuction(newfrom, newto);
							int i = 0;
							for(Auction a : set){
								ItemStack aitem = a.getItem();
								ItemMeta ameta = aitem.getItemMeta();
								SimpleDateFormat d = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
								Date date = new Date(a.getDate());
								String show = d.format(date);
								ameta.setDisplayName(Util.fixColor(Util.replaceString("&2&lMarket &8&l* &a" + ItemManager.get(aitem.getType()) + " &8&l*")));
								ameta.setLore(Arrays.asList(Util.fixColor(Util.replaceString("    &8&l>> &2Cena przedmiotu: &7" + a.getCost()))
										,Util.fixColor(Util.replaceString("    &8&l>> &2Wlasciciel: &7" + (UserManager.getUser(a.getOwner()) == null ? "" : UserManager.getUser(a.getOwner()).getLastName())))
										,Util.fixColor(Util.replaceString("    &8&l>> &2Data wystawienia: &7" + show))
										,Util.fixColor(Util.replaceString("    &8&l>> &2ID Aukcjii: &7" + a.getIndex()))));
								aitem.setItemMeta(ameta);
								inv.setItem(i, aitem);
								i++;
							}
							meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString("    &8&l* &6" + newfrom + "-" + newto))));
							item.setItemMeta(meta);
							ItemBuilder next = new ItemBuilder(Material.ARROW,1).setTitle(Util.fixColor(Util.replaceString("&8&l>> &2Nastepna Strona")))
									.addLore(Util.fixColor(Util.replaceString("    &8&l* &6" + newfrom + "-" + (newto-1))));
							inv.setItem(inv.getSize()-1, next.build());
							ItemBuilder back = new ItemBuilder(Material.ARROW,1).setTitle(Util.fixColor(Util.replaceString("&8&l>> &cPoprzednia Strona")))
									.addLore(Util.fixColor(Util.replaceString("    &8&l* &6" + newfrom + "-" + newto)));
							inv.setItem(inv.getSize()-2, back.build());
						}else{
							Player p = (Player)e.getWhoClicked();
							p.closeInventory();
							TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&cBrak mniejszej ilosci aukcjii !")));
						}
					}else{
						int auctionid = Integer.parseInt(meta.getLore().get(3).replace(Util.fixColor(Util.replaceString("    &8&l>> &2ID Aukcjii: &7")), ""));
						Auction a = AuctionManager.getAuction(auctionid);
						Player p = (Player) e.getWhoClicked();
						p.closeInventory();
						MarketUtil.OpenMenu_FinalBuy(p, a);
					}
				}
			}
		}else if(e.getInventory().getName().equalsIgnoreCase(Util.fixColor(Util.replaceString("&2%V% &cAuction &2%V%")))){
			e.setCancelled(true);
			ItemStack item = e.getCurrentItem();
			if (item != null) {
				ItemMeta meta = item.getItemMeta();
				if (meta != null) {
					if(meta.getDisplayName() != null && meta.getDisplayName().equals(Util.fixColor(Util.replaceString("&8&l>> &2Kup przedmiot z &a&lmarketu &8&l<<")))){
						Auction a = AuctionManager.getAuction(Integer.valueOf(e.getInventory().getItem(13).getItemMeta().getLore().get(1).replace(Util.fixColor(Util.replaceString("&8&l* &6ID Aukcjii: &7")), "")));
						Player p = (Player)e.getWhoClicked();
						User u = UserManager.getUser(p);
						if(u.getMoney() >= a.getCost()){
							if(!a.isSold()){
								User o = UserManager.getUser(a.getOwner());
								if(o != null){
									Auction a2 = AuctionManager.getAuction(Integer.valueOf(e.getInventory().getItem(13).getItemMeta().getLore().get(1).replace(Util.fixColor(Util.replaceString("&8&l* &6ID Aukcjii: &7")), "")));
									if(a2 == null){
										TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&cWykryto nieoczekiwany blad ! Sprobuj ponownie !")));
										return;
									}
									RedisClient.sendSectorsPacket(new AuctionSoldPacket(a.getIndex(), u.getLastName(), o.getUuid()));
									ItemStack itemStack = a.getItem();
									ItemMeta itemStackMeta = itemStack.getItemMeta();
									itemStackMeta.setDisplayName(null);
									itemStackMeta.setLore(null);
									itemStack.setItemMeta(itemStackMeta);
									ItemUtil.giveItems(Arrays.asList(itemStack), p);
									p.closeInventory();
									a.update(true);//TODO onDisable?
								}else{
									p.closeInventory();
									TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&cWykryto nieoczekiwany blad ! Spruboj ponownie !")));
								}
							}else{
								p.closeInventory();
								TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&cWykryto nieoczekiwany blad ! Spruboj ponownie !")));
							}
						}else{
							p.closeInventory();
							TitleUtil.sendFullTitle(p, 10, 100, 5, Util.fixColor(Util.replaceString("&4&l%X% &6&lMarket &4&l%X%")), Util.fixColor(Util.replaceString("&cBrak wystraczajacej ilosci pieniedzy !")));
						}
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void AsyncPlayerChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		if(temporarystorage.get(p) == null){
			return;
		}
		e.setCancelled(true);
		String message = e.getMessage();
		if(!Util.isInteger(message)){
			Util.sendMsg(p, Util.fixColor(Util.replaceString("&4Blad: &6Poprawny format wpisania ceny &8(&cnp.&8) &710,100,9230")));
			ItemStack is = temporarystorage.get(p);
			ItemMeta ism = is.getItemMeta();
			ism.setDisplayName(null);
			ism.setLore(null);
			is.setItemMeta(ism);
			p.getInventory().addItem(is);
			temporarystorage.remove(p);
			return;
		}
		int cost = Integer.valueOf(message);
		if(cost <= 0){
			Util.sendMsg(p, Util.fixColor(Util.replaceString("&4Blad: &6Poprawny format wpisania ceny &8(&cnp.&8) &710,100,9230")));
			ItemStack is = temporarystorage.get(p);
			ItemMeta ism = is.getItemMeta();
			ism.setDisplayName(null);
			ism.setLore(null);
			is.setItemMeta(ism);
			p.getInventory().addItem(is);
			temporarystorage.remove(p);
			return;
		}
		ItemStack item = temporarystorage.get(p);
		MarketUtil.OpenMenu_Second(p, item, cost);
	}
}