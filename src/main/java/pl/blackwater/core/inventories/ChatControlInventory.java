package pl.blackwater.core.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.ChatControlUser;
import pl.blackwater.core.managers.ChatControlUserManager;
import pl.blackwaterapi.gui.actions.IAction;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;

public class ChatControlInventory
{
	
	public static void openChatControlInventory(Player p)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString("&6Zarzadzanie Wiadomosciami")), 3);
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.fixColor("&8» &6Powrot &8«"));
		final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle("§6");
		final ItemBuilder lightblue = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)3).setTitle("§6");
		final ItemBuilder next = new ItemBuilder(Material.STAINED_CLAY,1,(short)4).setTitle("§8» §2Nastepna Strona §8«");

		final ChatControlUser cc = ChatControlUserManager.getUser(p);
		cc.getItem(cc.isDeathMessage(), ChatControlUser.getDeathMessageTitle(), inv, 9,0, (arg0, inv1, slot, item) -> {
			ItemStack glass = inv1.getItem(slot + 9);
			cc.toggleDeathMessage(item, glass);

		});
		cc.getItem(cc.isKillKeyMessage(), ChatControlUser.getKillKeyMessageTitle(), inv, 10,0, (arg0, inv12, slot, item) -> {
			ItemStack glass = inv12.getItem(slot + 9);
			cc.toggleKillKeyMessage(item, glass);

		});
		cc.getItem(cc.isKillDiamondMessage(), ChatControlUser.getKillDiamondMessageTitle(), inv, 11,0, (arg0, inv13, slot, item) -> {
			ItemStack glass = inv13.getItem(slot + 9);
			cc.toggleKillDiamondMessage(item, glass);

		});
		cc.getItem(cc.isKillTurboMoneyMessage(), ChatControlUser.getKillTurboMoneyMessageTitle(), inv, 12,0, (arg0, inv14, slot, item) -> {
			ItemStack glass = inv14.getItem(slot + 9);
			cc.toggleKillTurboMoneyMessage(item, glass);

		});
		cc.getItem(cc.isKillKillStreakMessage(), ChatControlUser.getKillKillStreakMessageTitle(), inv, 13, 0, (arg0, inv15, slot, item) -> {
			ItemStack glass = inv15.getItem(slot + 9);
			cc.toggleKillKillStreakMessage(item, glass);

		});
		cc.getItem(cc.isKillMoneyLevelMessage(), ChatControlUser.getKillMoneyLevelMessageTitle(), inv, 14, 0, (arg0, inv16, slot, item) -> {
			ItemStack glass = inv16.getItem(slot + 9);
			cc.toggleKillMoneyLevelMessage(item, glass);

		});
		inv.setItem(16-9, lightblue.build(),null);
		inv.setItem(16, next.build(), (p1, arg1, arg2, arg3) -> {
			p1.closeInventory();
			openChatControlSecondInventory(p1);

		});
		inv.setItem(16+9, lightblue.build(),null);
		inv.setItem(17-9, purple.build(),null);
		inv.setItem(17, back.build(),null);
		inv.setItem(17+9, purple.build(),null);
		p.openInventory(inv.get());
	}
	private static void openChatControlSecondInventory(Player p)
	{
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString("&6Zarzadzanie Wiadomosciami")), 3);
		final ItemBuilder back = new ItemBuilder(Material.WOOL,1,(short)14).setTitle(Util.fixColor("&8» &4Powrot &8«"));
		final ItemBuilder purple = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle("§6");
		final ChatControlUser cc = ChatControlUserManager.getUser(p);
		cc.getItem(cc.isCaseOpenMessage(), ChatControlUser.getCaseOpenTitle(), inv, 9,1, (arg0, inv1, slot, item) -> {
			final ItemStack glass = inv1.getItem(slot + 9);
			  cc.toggleCaseOpenMessage(item, glass);

		});
		cc.getItem(cc.isItemShopMessage(), ChatControlUser.getItemShopMessageTitle(), inv, 10, 1, (arg0, inv12, slot, item) -> {
			final ItemStack glass = inv12.getItem(slot + 9);
			  cc.toggleItemShopMessage(item, glass);

		});
		cc.getItem(cc.isKillStreakGlobalMessage(), ChatControlUser.getKillStreakGlobalMessageTitle(), inv, 11, 1, (arg0, inv13, slot, item) -> {
			final ItemStack glass = inv13.getItem(slot + 9);
			  cc.toggleKillStreakGlobalMessage(item, glass);

		});
		cc.getItem(cc.isAutoMessage(), ChatControlUser.getAutoMessageGlobalTitle(), inv, 12, 1, (arg0, inv14, slot, item) -> {
			final ItemStack glass = inv14.getItem(slot + 9);
			  cc.toggleAutoMessage(item, glass);

		});
		cc.getItem(cc.isMarketGlobalMessage(), ChatControlUser.getMarketMessageGlobalTitle(), inv, 13, 1, (arg0, inv15, slot, item) -> {
			final ItemStack glass = inv15.getItem(slot + 9);
			  cc.toggleMarketMessage(item, glass);

		});
		cc.getItem(cc.isHeadHunterGlobalMessage(), ChatControlUser.getHeadhunterMessageGlobalTitle(), inv, 14, 1, (arg0, inv16, slot, item) -> {
			final ItemStack glass = inv16.getItem(slot + 9);
			  cc.toggleHeadHunterMessage(item, glass);

		});
		inv.setItem(17-9, purple.build(),null);
		inv.setItem(17, back.build(), (p1, arg1, arg2, arg3) -> {
			p1.closeInventory();
			openChatControlInventory(p1);

		});
		inv.setItem(17+9, purple.build(),null);
		p.openInventory(inv.get());
	}
	
	
}
