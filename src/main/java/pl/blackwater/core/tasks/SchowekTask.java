package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.utils.ItemStackUtil;
import pl.blackwaterapi.utils.Util;

public class SchowekTask extends BukkitRunnable implements Colors{

	@Override
	public void run() {
		if(CoreConfig.ENABLEMANAGER_SAFE){
			for (Player p : Bukkit.getOnlinePlayers()){
				final int lk = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE, p,(short)1);
				final int lr = ItemStackUtil.getAmountOfItem(Material.GOLDEN_APPLE, p, (short)0);
				final int lp = ItemStackUtil.getAmountOfItem(Material.ENDER_PEARL, p,(short)0);
				final int ls = ItemStackUtil.getAmountOfItem(Material.SNOW_BALL,p,(short)0);
				final int la = ItemStackUtil.getAmountOfItem(Material.ARROW,p,(short)0);

				if (lk > CoreConfig.LIMITMANAGER_KOX){
					final User u = UserManager.getUser(p);
					final ItemStack goldenapple_1 = new ItemStack(Material.GOLDEN_APPLE, lk - CoreConfig.LIMITMANAGER_KOX,(short)1);
					removeItems(p.getInventory(),Material.GOLDEN_APPLE,(short)1,lk - CoreConfig.LIMITMANAGER_KOX);
					u.addSafeKox(lk - CoreConfig.LIMITMANAGER_KOX);
					final int i2 = lk - CoreConfig.LIMITMANAGER_KOX; 
					Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Posiadasz przy sobie zbyt duza ilosc " + ChatColor.GOLD + "koxow! " + ImportantColor + i2 + MainColor + " zostaly przeniesione do schowka " + SpecialSigns + "(" + WarningColor + "/schowek" + SpecialSigns + ")"));
				}
				if (lr > CoreConfig.LIMITMANAGER_REF){
					final User u = UserManager.getUser(p);
					final ItemStack goldenapple = new ItemStack(Material.GOLDEN_APPLE, lr - CoreConfig.LIMITMANAGER_REF);
					removeItems(p.getInventory(),Material.GOLDEN_APPLE,(short)0,lr - CoreConfig.LIMITMANAGER_REF);

					u.addSafeRef(lr - CoreConfig.LIMITMANAGER_REF);
					final int i2 = lr - CoreConfig.LIMITMANAGER_REF; 
					Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Posiadasz przy sobie zbyt duza ilosc " + ChatColor.YELLOW + "refili! " + ImportantColor + i2 + MainColor + " zostaly przeniesione do schowka " + SpecialSigns + "(" + WarningColor + "/schowek" + SpecialSigns + ")"));
				}
				if (lp > CoreConfig.LIMITMANAGER_PEARL){
					final User u = UserManager.getUser(p);
					final ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, lp - CoreConfig.LIMITMANAGER_PEARL);
					removeItems(p.getInventory(),Material.ENDER_PEARL,(short)0,lp - CoreConfig.LIMITMANAGER_PEARL);
					u.addSafePearl(lp - CoreConfig.LIMITMANAGER_PEARL);
					final int i2 = lp - CoreConfig.LIMITMANAGER_PEARL; 
					Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Posiadasz przy sobie zbyt duza ilosc " + ChatColor.LIGHT_PURPLE + "perel! " + ImportantColor + i2 + MainColor + " zostaly przeniesione do schowka " + SpecialSigns + "(" + WarningColor + "/schowek" + SpecialSigns + ")"));
				}
				if(ls > CoreConfig.LIMITMANAGER_SNOWBALL){
					final User u = UserManager.getUser(p);
					final ItemStack enderpearl = new ItemStack(Material.SNOW_BALL, ls - CoreConfig.LIMITMANAGER_SNOWBALL);
					removeItems(p.getInventory(),Material.SNOW_BALL,(short)0,ls - CoreConfig.LIMITMANAGER_SNOWBALL);
					u.addSafeSnowBall(ls - CoreConfig.LIMITMANAGER_SNOWBALL);
					final int i2 = ls - CoreConfig.LIMITMANAGER_SNOWBALL;
					Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Posiadasz przy sobie zbyt duza ilosc " + ChatColor.DARK_BLUE + "sniezek! " + ImportantColor + i2 + MainColor + " zostaly przeniesione do schowka " + SpecialSigns + "(" + WarningColor + "/schowek" + SpecialSigns + ")"));
				}
				if(la > CoreConfig.LIMITMANAGER_ARROWS){
					final User u = UserManager.getUser(p);
					final ItemStack arrows = new ItemStack(Material.ARROW, la - CoreConfig.LIMITMANAGER_ARROWS);
					removeItems(p.getInventory(),Material.ARROW,(short)0,la - CoreConfig.LIMITMANAGER_ARROWS);
					u.addSafeArrow(la - CoreConfig.LIMITMANAGER_ARROWS);
					final int i2 = la - CoreConfig.LIMITMANAGER_ARROWS;
					Util.sendMsg(p, Util.replaceString(SpecialSigns + "* " + MainColor + "Posiadasz przy sobie zbyt duza ilosc " + ChatColor.WHITE+ "strzal! " + ImportantColor + i2 + MainColor + " zostaly przeniesione do schowka " + SpecialSigns + "(" + WarningColor + "/schowek" + SpecialSigns + ")"));
				}
			}	
		}
	}
	public static void removeItems(Inventory inventory, Material type,short data, int amount) {
		if (amount <= 0) return;
		int size = inventory.getSize();
		for (int slot = 0; slot < size; slot++) {
			ItemStack is = inventory.getItem(slot);
			if (is == null) continue;
			if (type == is.getType() && data == is.getDurability()) {
				int newAmount = is.getAmount() - amount;
				if (newAmount > 0) {
					is.setAmount(newAmount);
					break;
				} else {
					inventory.clear(slot);
					amount = -newAmount;
					if (amount == 0) break;
				}
			}
		}
	}

}
