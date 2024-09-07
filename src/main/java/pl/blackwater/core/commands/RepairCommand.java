package pl.blackwater.core.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.data.User;
import pl.blackwater.core.inventories.RepairInventory;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

public class RepairCommand extends PlayerCommand
{
	public RepairCommand() {
		super("repair", "naprawianie itemow", "/repair", "core.repair");
	}

	public boolean onCommand(Player p, String[] args)
	{
		RepairInventory.openRepairMenu(p);
		return true;
		/*User u = UserManager.getUser(p);
		if (p.hasPermission("core.vip")){
			if (u.getMoney() >= CoreConfig.REPAIRMANAGER_MONEY_VIP){
				ItemStack item = p.getItemInHand();
				if ((item.getType().isBlock()) || (item.getType().equals(Material.AIR)) || (item.getType().equals(Material.GOLDEN_APPLE))) {
					return Util.sendMsg(p, "&4Blad: &cNie mozesz naprawic tego przemiotu!");
				}
				if (item.getDurability() == 0) {
					return Util.sendMsg(p, "&4Blad: &cTen przedmiot jest naprawiony");
				}
				item.setDurability((short)0);
				u.removeMoney(CoreConfig.REPAIRMANAGER_MONEY_SVIP);
				return Util.sendMsg(p, "&6Naprawiles przdmiot &c" + ItemUtil.getPolishMaterial(item.getType()));
			}else {
				p.sendMessage(Util.fixColor("&4Blad: &7Aby naprawic ten przedmiot musisz miec " + CoreConfig.REPAIRMANAGER_MONEY_VIP + " kasy"));
			}
		}else
		if (p.hasPermission("core.svip")){
			if (u.getMoney() >= CoreConfig.REPAIRMANAGER_MONEY_SVIP){
				ItemStack item = p.getItemInHand();
				if ((item.getType().isBlock()) || (item.getType().equals(Material.AIR)) || (item.getType().equals(Material.GOLDEN_APPLE))) {
					return Util.sendMsg(p, "&4Blad: &cNie mozesz naprawic tego przemiotu!");
				}
				if (item.getDurability() == 0) {
					return Util.sendMsg(p, "&4Blad: &cTen przedmiot jest naprawiony");
				}
				item.setDurability((short)0);
				u.removeMoney(CoreConfig.REPAIRMANAGER_MONEY_SVIP);
				return Util.sendMsg(p, "&6Naprawiles przdmiot &c" + ItemUtil.getPolishMaterial(item.getType()));
			}else {
				p.sendMessage(Util.fixColor("&4Blad: &7Aby naprawic ten przedmiot musisz miec " + CoreConfig.REPAIRMANAGER_MONEY_SVIP + " kasy"));
			}
		}else if (u.getMoney() >= CoreConfig.REPAIRMANAGER_MONEY_DEFAULT){
			ItemStack item = p.getItemInHand();
			if ((item.getType().isBlock()) || (item.getType().equals(Material.AIR)) || (item.getType().equals(Material.GOLDEN_APPLE))) {
				return Util.sendMsg(p, "&4Blad: &cNie mozesz naprawic tego przemiotu!");
			}
			if (item.getDurability() == 0) {
				return Util.sendMsg(p, "&4Blad: &cTen przedmiot jest naprawiony");
			}
			item.setDurability((short)0);
			u.removeMoney(CoreConfig.REPAIRMANAGER_MONEY_DEFAULT);
			return Util.sendMsg(p, "&6Naprawiles przdmiot &c" + ItemUtil.getPolishMaterial(item.getType()));
		}else {
			p.sendMessage(Util.fixColor("&4Blad: &7Aby naprawic ten przedmiot musisz miec " + CoreConfig.REPAIRMANAGER_MONEY_DEFAULT + " kasy"));
		}
		return false;*/
	}
}

