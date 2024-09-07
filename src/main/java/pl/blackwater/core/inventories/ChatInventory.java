package pl.blackwater.core.inventories;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.ChatManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.chat.ChatTogglePacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.ArrayList;
import java.util.List;

public class ChatInventory implements Colors
{
	@Getter private static final List<Player> changeKillsLimit = new ArrayList<>();
	public static void openMenu(Player p)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + ImportantColor + "ChatManager" + SpecialSigns + " *")), 1);
		final ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)7).setTitle("&6");
		for(int i = 0; i < inv.get().getSize(); i ++){
			inv.setItem(i, air.build(), null);
		}
		final ItemBuilder clear = new ItemBuilder(Material.GLASS).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Wyczysc " + ImportantColor + "chat" + SpecialSigns + " <<")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij LPM aby wczyscic chat")));
		inv.setItem(0, clear.build(), (p1, inv1, position, item) -> {
			ChatManager manager = Core.getChatManager();
			manager.clearChat(p1.getName());
			p1.closeInventory();
		});
		final ItemBuilder toggle = new ItemBuilder(Material.LEVER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Wlacz/Wylacz " + ImportantColor + "chat" + SpecialSigns + " <<")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij LPM aby wylaczyc/wlaczyc chat")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Status: " + (Core.getChatManager().isChat() ? ChatColor.GREEN + "wlaczony" : ChatColor.DARK_RED + "wylaczony"))));
		inv.setItem(2, toggle.build(), (p12, arg1, arg2, arg3) -> {
			ChatManager manager = Core.getChatManager();
			final ChatTogglePacket packet = new ChatTogglePacket(p12.getName(), !manager.isChat());
			RedisClient.sendSectorsPacket(packet);
			p12.closeInventory();

		});
		final ItemBuilder togglevip = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Wlacz/Wylacz " + ImportantColor + "chat VIP" + SpecialSigns + " <<")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij LPM aby wylaczyc/wlaczyc chat VIP")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Status: " + (Core.getChatManager().isVipChat() ? ChatColor.GREEN + "wlaczony" : ChatColor.DARK_RED + "wylaczony"))));
		inv.setItem(8, togglevip.build(), (p13, arg1, arg2, arg3) -> {
			ChatManager manager = Core.getChatManager();
			manager.toggleVipChat(p13.getName(), !manager.isVipChat());
			p13.closeInventory();

		});
		final ItemBuilder slowmodetime = new ItemBuilder(Material.PAPER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ustaw " + ImportantColor + "limit czasowy chatu" + SpecialSigns + " <<")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Kliknij LPM aby ustawic limit czasu")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktualny Limit: " + ImportantColor + CoreConfig.CHATMANAGER_SLOWMODE)));
		inv.setItem(6, slowmodetime.build(), (p14, arg1, arg2, arg3) -> {
			p14.closeInventory();
			openSlowModeMenu(p14);
		});
		final ItemBuilder killstowrite = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ustaw limit " + ImportantColor + "zabojstw " + MainColor + "do pisania na chacie")))
				.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktualny limit: " + ImportantColor + CoreConfig.CHATMANAGER_KILLS)));
		inv.setItem(4, killstowrite.build(), (p15, arg1, arg2, arg3) -> {
			p15.closeInventory();
			changeKillsLimit.add(p15);
			ActionBarUtil.sendActionBar(p15, Util.fixColor(Util.replaceString(WarningColor + "Napisz " + WarningColor_2 + "liczbe " + WarningColor + "lub " + WarningColor_2 + "'cancel'")));
		});
		inv.openInventory(p);
	}
	private static void openSlowModeMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + ImportantColor + "Ustaw limit czasu" + SpecialSigns + " *")), 1);
		final ItemBuilder amount = new ItemBuilder(Material.PAPER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit: " + ImportantColor + CoreConfig.CHATMANAGER_SLOWMODE)))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Kliknij LPM aby zaakceptowac limit!")));
		final ItemBuilder add = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)13).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Dodaj limit " + ImportantColor + "+1")));
		final ItemBuilder remove = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)14).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Odejmij limit " + ImportantColor + "-1")));
		final ItemBuilder broadcast = new ItemBuilder(Material.STAINED_CLAY,1,(short)5).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Czy ma wyslac wiadomosc globalna?")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Zielony: " + ChatColor.GREEN + "%V%")))
					.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Czerwony: " + ChatColor.DARK_RED + "%X%")));
		inv.setItem(0, add.build(), (p, inv1, arg2, arg3) -> {
			final ItemStack main = inv1.getItem(3);
			final ItemMeta mainmeta = main.getItemMeta();
			int actually = Integer.parseInt(mainmeta.getDisplayName().replace(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit: " + ImportantColor)), ""));
			actually++;
			mainmeta.setDisplayName(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit: " + ImportantColor + actually)));
			main.setItemMeta(mainmeta);
		});
		inv.setItem(3, amount.build(), (p, inv12, arg2, item) -> {
			final int actually = Integer.parseInt(item.getItemMeta().getDisplayName().replace(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit: " + ImportantColor)), ""));
			CoreConfig config = new CoreConfig();
			config.setField("chatmanager.slowmode", actually);
			CoreConfig.CHATMANAGER_SLOWMODE = actually;
			p.closeInventory();
			openMenu(p);
			final boolean bc = (inv12.getItem(5).getDurability() == (short)5);
			if(bc)
				Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Administrator " + ImportantColor + p.getName() + MainColor + " ustawil nowy limit czasowy pisania na chacie " + SpecialSigns + "(" + ImportantColor + UnderLined + actually + "s" + SpecialSigns + ")")));
		});
		inv.setItem(5, broadcast.build(), (arg0, arg1, arg2, item) -> {
			final short s = (item.getDurability() == 5 ? (short)14 : (short)5);
			item.setDurability(s);
		});
		inv.setItem(8, remove.build(), (p, inv13, arg2, arg3) -> {
			final ItemStack main = inv13.getItem(3);
			final ItemMeta mainmeta = main.getItemMeta();
			int actually = Integer.parseInt(mainmeta.getDisplayName().replace(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit: " + ImportantColor)), ""));
			actually--;
			mainmeta.setDisplayName(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Aktualny limit: " + ImportantColor + actually)));
			main.setItemMeta(mainmeta);
		});
		
		inv.openInventory(player);
	}

}
