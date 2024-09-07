package pl.blackwater.storage.inventories;

import com.sk89q.worldguard.LocalPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.TeleportManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.storage.data.StorageRent;
import pl.blackwater.storage.managers.StorageRentManager;
import pl.blackwater.storage.settings.StorageConfig;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.timer.TimerUtil;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.impl.storage.StorageAddExpireTimePacket;
import pl.justsectors.packets.impl.storage.StorageBuyPacket;
import pl.justsectors.packets.impl.storage.StorageLeftPacket;
import pl.justsectors.packets.impl.storage.StorageRemoveMemberPacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.SectorManager;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class StorageRentInventory implements Colors{
	@SuppressWarnings("deprecation")
	public static void openBoughtStorage(final StorageRent storage, final User u, Player p) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Magazyn " + ImportantColor + storage.getStorageRegionName())), 1);
		final ItemBuilder leave = new ItemBuilder(Material.BARRIER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Opusc " + WarningColor + "magazyn")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Pieniadze za kupno nie zostana zwrocone!")));
		inv.setItem(8, leave.build(), (p1, arg1, arg2, arg3) -> {
			final InventoryGUI inv1 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Magazyn " + ImportantColor + storage.getStorageRegionName())), 1);
			final ItemBuilder accept = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(ChatColor.DARK_GREEN + "POTWIERDZ"));
			final ItemBuilder decline = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(Util.fixColor(ChatColor.DARK_RED + "ANULUJ"));
			inv1.setItem(0, accept.build(), (p11, arg11, arg21, arg31) -> {
				final StorageLeftPacket packet = new StorageLeftPacket(u.getUuid(),storage.getId());
				RedisClient.sendSectorsPacket(packet);
				p11.closeInventory();
				ActionBarUtil.sendActionBar(p11, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + WarningColor + "Opuszczono " + MainColor + "magazyn")));
			});
			inv1.setItem(8, decline.build(), (p112, arg112, arg212, arg312) -> {
				p112.closeInventory();
				openBoughtStorage(storage, u, p112);
			});
			inv1.openInventory(p1);
		});
		final ItemBuilder teleport = new ItemBuilder(Material.ENDER_PORTAL_FRAME).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ImportantColor + "Teleportacja " + MainColor + "do magazynu")));
		inv.setItem(0, teleport.build(), (p12, arg1, arg2, arg3) -> {
			p12.closeInventory();
			TeleportManager.teleportPlayer(u,storage.getTeleportLocation(), SectorManager.getSector("magazyn"));
		});
		final ItemBuilder expiretime = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ChatColor.GOLD + "Przedluz " + MainColor + "czas wynajmu magazynu")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Przedluzenie magazynu o " + ImportantColor + "1 dzien " + MainColor + "za " + ImportantColor + StorageConfig.STORAGE_DAYCOST + "$")));
		inv.setItem(4, expiretime.build(), (p13, arg1, arg2, arg3) -> {
			if(u.getMoney() >= StorageConfig.STORAGE_DAYCOST) {
				if(storage.getExpireTime() + TimeUtil.DAY.getTime(1) < System.currentTimeMillis() + TimeUtil.DAY.getTime(StorageConfig.STORAGE_MAXEXPIRETIME)){
					final StorageAddExpireTimePacket packet = new StorageAddExpireTimePacket(u.getUuid(),storage.getId());
					RedisClient.sendSectorsPacket(packet);
					p13.closeInventory();
					ActionBarUtil.sendActionBar(p13, Util.fixColor(Util.replaceString(ChatColor.GOLD + "Przedluzono " + MainColor + "magazyn o 1 dzien")));
				}else {
					Util.sendMsg(p13, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Magazyn nie moze byc aktywny przez wiecej niz " + StorageConfig.STORAGE_MAXEXPIRETIME + " dni"));
					p13.closeInventory();
				}
			}else {
				Util.sendMsg(p13, Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wystarczajacej ilosci pieniedzy!"));
				p13.closeInventory();
			}

		});
		final ItemBuilder addmember = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ChatColor.DARK_GREEN + "Dodaj " + MainColor + "gracza do magazynu!")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Dodanie czlonka magazynu kosztuje " + ImportantColor + StorageConfig.STORAGE_ADDMEMBERCOST + "$")));
		inv.setItem(2, addmember.build(), (p14, arg1, arg2, arg3) -> {
			p14.closeInventory();
			StorageRentManager.getAddMembersAction().put(p14, storage);
			TitleUtil.sendFullTitle(p14, 15, 10, 20, "", Util.fixColor(ChatColor.DARK_GREEN + "Napisz " + MainColor + "nick gracza ktorego chcesz dodac na chacie "));
		});
		final ItemBuilder removemember = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ChatColor.DARK_RED + "Usun " + MainColor + "gracza z magazynu!")));
		inv.setItem(6, removemember.build(), (p15, arg1, arg2, arg3) -> {
			p15.closeInventory();
			final InventoryGUI inv12 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Magazyn " + ImportantColor + storage.getStorageRegionName())), 6);
			int position = 0;
			for(String uuid : storage.getMembersList()) {
				final User other = UserManager.getUser(UUID.fromString(uuid));
				if(other != null) {
					if (!other.equals(u)) {
						final ItemStack skull = ItemUtil.getPlayerHead(other.getLastName());
						final ItemMeta meta = skull.getItemMeta();
						meta.setDisplayName(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ImportantColor + other.getLastName())));
						meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Kliknij aby " + ChatColor.DARK_RED + "usunac " + MainColor + "tego gracza z magazynu!"))));
						skull.setItemMeta(meta);
						inv12.setItem(position, skull, (p151, arg113, arg213, arg313) -> {
							p151.closeInventory();
							final StorageRemoveMemberPacket packet = new StorageRemoveMemberPacket(other.getUuid(), storage.getId());
							RedisClient.sendSectorsPacket(packet);
							TitleUtil.sendFullTitle(p151, 15, 10, 10, "", Util.fixColor(Util.replaceString(ChatColor.DARK_RED + "Usunieto " + MainColor + "gracza z magazynu!")));
						});
						position++;
					}
				}
			}
			inv12.openInventory(p15);
		});
		p.openInventory(inv.get());

	}
	public static void openMenu(Player p) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "MAGAZYNY" + SpecialSigns + " <<-|")), 1);
		final ItemBuilder buystorage = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + ChatColor.DARK_GREEN + "Kup " + MainColor + "wolny magazyn!")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Po kliknieciu dostaniesz pierwszy wolny magazyn na serwerze!")));
		final User u = UserManager.getUser(p);
		inv.setItem(1, buystorage.build(), (p1, arg1, arg2, arg3) -> {
			p1.closeInventory();
			final StorageRent storage = StorageRentManager.getFreeStorageRent();
			if(storage == null) {
				TitleUtil.sendFullTitle(p1, 15, 10, 15, "", Util.fixColor(WarningColor + "Blad: " + WarningColor_2 + "Brak wolnych magazynow na serwerze"));
				return;
			}
			if(u.getMoney() >= storage.getCost()) {
				final StorageBuyPacket storageBuyPacket =  new StorageBuyPacket(u.getUuid(),storage.getId());
				RedisClient.sendSectorsPacket(storageBuyPacket);
				ActionBarUtil.sendActionBar(p1, Util.fixColor(Util.replaceString(MainColor + "Zakupiono magazyn " + ChatColor.GREEN + "%V%")));
			}else
				Util.sendMsg(p1, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wystarczajacej liczby monet " + SpecialSigns + "(" + WarningColor + storage.getCost() + "$" + SpecialSigns + ")");
		});
		final ItemBuilder openstoragelist = new ItemBuilder(Material.BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Otworz liste swoich magazynow")));
		inv.setItem(7, openstoragelist.build(), (p12, arg1, arg2, arg3) -> {
			final Set<StorageRent> storages = StorageRentManager.getUserStorages(u);
			p12.closeInventory();
			if(storages.size() < 1) {
				Util.sendMsg(p12, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz zadnego magazynu na serwerze!");
				return;
			}
			final InventoryGUI inv1 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "MAGAZYNY" + SpecialSigns + " <<-|")), 1);
			int position = 0;
			for(final StorageRent storage : storages) {
				final ItemBuilder storageitem = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + WarningColor + storage.getStorageRegionName())));
				inv1.setItem(position, storageitem.build(), (p121, arg11, arg21, arg31) -> {
					p121.closeInventory();
					openBoughtStorage(storage, u, p121);
				});
				position++;
			}
			inv1.openInventory(p12);
		});
		final ItemBuilder openstoragememberlist = new ItemBuilder(Material.PAPER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Otworz liste magazynow w ktorych jestes czlonkiem")));
		inv.setItem(4, openstoragememberlist.build(), (p13, arg1, arg2, arg3) -> {
			p13.closeInventory();
			final User user = UserManager.getUser(p13.getDisplayName());
			final InventoryGUI inv12 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "MAGAZYNY" + SpecialSigns + " <<-|")), 1);
			int position = 0;
			for(final StorageRent storage : StorageRentManager.getStoragerents().values()) {
				if(storage.getMembers().contains(user.getUuid().toString())){
					final ItemBuilder storageitem = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + WarningColor + storage.getStorageRegionName())));
					inv12.setItem(position, storageitem.build(), (p131, arg112, arg212, arg312) -> {
						p131.closeInventory();
						TeleportManager.teleportPlayer(u,storage.getTeleportLocation(), SectorManager.getSector("magazyn"));
					});
					position++;
				}
			}
			inv12.openInventory(p13);
		});
		p.openInventory(inv.get());
	}

}
