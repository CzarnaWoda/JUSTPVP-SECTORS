package pl.blackwater.core.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.ItemManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;

import java.util.Collections;

public class RepairInventory implements Colors
{

	public static void openRepairMenu(Player p)
	{
		final ItemStack item = p.getItemInHand();
			if(ItemManager.isRepairable(item)) {
				if ((item.getType().isBlock()) || (item.getType().equals(Material.AIR)) || (item.getType().equals(Material.GOLDEN_APPLE))) {
					Util.sendMsg(p, "&4Blad: &cNie mozesz naprawic tego przemiotu!");
					return;
					}
					if (item.getDurability() == 0) {
						Util.sendMsg(p, "&4Blad: &cTen przedmiot jest naprawiony");
						return;
					}
					final int expcost = (p.hasPermission("core.svip") ? CoreConfig.REPAIRMANAGER_LEVEL_SVIP : (p.hasPermission("core.vip") ? CoreConfig.REPAIRMANAGER_LEVEL_VIP : CoreConfig.REPAIRMANAGER_LEVEL_DEFAULT));
					final int moneycost = (p.hasPermission("core.svip") ? CoreConfig.REPAIRMANAGER_MONEY_SVIP : (p.hasPermission("core.vip") ? CoreConfig.REPAIRMANAGER_MONEY_VIP : CoreConfig.REPAIRMANAGER_MONEY_DEFAULT));
					InventoryGUI inv1 = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Naprawa " + ImportantColor + "przedmiotow" + SpecialSigns + " *")), 1);
					final ItemBuilder exprepair = new ItemBuilder(Material.EXP_BOTTLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Naprawa przedmiotu za " + ImportantColor + "EXP")))
								.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    " + "* " + MainColor + "Koszt: " + ImportantColor + UnderLined + expcost + MainColor + " LvL")));
					final ItemBuilder moneyrepair = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Naprawa przedmiotu za " + ImportantColor + "kase")))
								.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "    " + "* " + MainColor + "Koszt: " + ImportantColor + UnderLined + moneycost + MainColor + "$")));
					final ItemBuilder cancell = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Kliknij aby " + WarningColor + BOLD + "ANULOWAC")));
					inv1.setItem(0, exprepair.build(), (p1, arg11, arg21, arg3) -> {
						if(p1.getLevel() >= expcost){
							p1.setLevel(p1.getLevel() - expcost);
							item.setDurability((short)0);
							p1.closeInventory();
							p1.updateInventory();
							TitleUtil.sendFullTitle(p1, 20, 100, 30, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Naprawianie " + ImportantColor + "przedmiotu" +  SpecialSigns + " <<")), Util.fixColor(Util.replaceString(MainColor  + "Naprawiono przedmiot " + ImportantColor + ItemManager.get(item.getType()))));
						}else{
							p1.closeInventory();
							TitleUtil.sendFullTitle(p1, 20, 100, 30, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Naprawianie " + ImportantColor + "przedmiotu" +  SpecialSigns + " <<")), Util.fixColor(Util.replaceString(MainColor  + "Nie posiadasz wystarczajaco " + ImportantColor + "EXP")));
						}

					});
					inv1.setItem(8, moneyrepair.build(), (p12, arg112, arg212, arg3) -> {
						User u = UserManager.getUser(p12);
						if(u.getMoney() >= moneycost){
							u.removeMoneyViaPacket(moneycost);
							item.setDurability((short)0);
							p12.closeInventory();
							p12.updateInventory();
							TitleUtil.sendFullTitle(p12, 20, 100, 30, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Naprawianie " + ImportantColor + "przedmiotu" +  SpecialSigns + " <<")), Util.fixColor(Util.replaceString(MainColor  + "Naprawiono przedmiot " + ImportantColor + ItemManager.get(item.getType()))));
						}else{
							p12.closeInventory();
							TitleUtil.sendFullTitle(p12, 20, 100, 30, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Naprawianie " + ImportantColor + "przedmiotu" +  SpecialSigns + " <<")), Util.fixColor(Util.replaceString(MainColor  + "Nie posiadasz wystarczajaco " + ImportantColor + "pieniedzy")));
						}

					});
					inv1.setItem(4, cancell.build(), (p13, arg113, arg213, arg3) -> {
						p13.closeInventory();
						TitleUtil.sendFullTitle(p13, 20, 100, 30, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Naprawianie " + ImportantColor + "przedmiotu" +  SpecialSigns + " <<")), Util.fixColor(Util.replaceString(MainColor  + "Anulowano naprawe " + ImportantColor + "przedmiotu")));
					});
					inv1.openInventory(p);
			}else{
				Util.sendMsg(p, "&4Blad: &cNie mozesz naprawic tego przemiotu!");
				return;
			}
	}

}
