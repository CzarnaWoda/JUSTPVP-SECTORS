package pl.blackwater.core.inventories;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.utils.GuildTimeUtil;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

public class RankingInventory implements Colors
{
	public static void openMenu(final Player viewer,final User ouser){
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Statystyki gracza " + ImportantColor +  ouser.getLastName() + SpecialSigns + " <<")), 6);
		final ItemBuilder a = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "PvP" + SpecialSigns + " <<")))
					.addLores(Arrays.asList(Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Zabojstwa: " + ImportantColor + BOLD + ouser.getKills())
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Smierci: " + ImportantColor + BOLD + ouser.getDeaths())
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Stosunek K/D: " + ImportantColor + BOLD + ouser.getKd())
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Level: " + ImportantColor + BOLD + ouser.getLevel())));
		final Guild g = ouser.getGuild();
		final ItemBuilder b = new ItemBuilder(Material.ENDER_PORTAL_FRAME).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Gildia" + SpecialSigns + " <<")))
					.addLores(Arrays.asList(Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Tag: " + ImportantColor + BOLD + (g == null ? "Brak" : g.getTag())) 
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Nazwa: " + ImportantColor + BOLD + (g == null ? "Brak" : g.getName())) 
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Punkty: " + ImportantColor + BOLD + (g == null ? "0" : g.getPoints()))));
		final ItemBuilder d = new ItemBuilder(Material.BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Statystyki" + SpecialSigns + " <<")))
					.addLores(Arrays.asList(Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Rzucone perly: " + ImportantColor + BOLD + ouser.getThrowPearl())
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Zjedzone koxy: " + ImportantColor + BOLD + ouser.getEatKox())
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Zjedzone refile: " + ImportantColor + BOLD + ouser.getEatRef())
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Otworzone skrzynki: " + ImportantColor + BOLD + ouser.getOpenPremiumChest())));
		final ItemStack skull = ItemUtil.getPlayerHead(ouser.getLastName());
		final SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
		skullMeta.setDisplayName(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + ouser.getLastName() + SpecialSigns + " <<")));
		skull.setItemMeta(skullMeta);
		final ItemBuilder f = new ItemBuilder(Material.ANVIL).setTitle(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Zdobyte Boosty" + SpecialSigns + " <<"))
					.addLores(Arrays.asList(Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "KoxPvP: " + ImportantColor + BOLD + ouser.getBoostKox() + SpecialSigns + "/" + ImportantColor + BOLD + "3")
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "UltraExp: " + ImportantColor + BOLD + ouser.getBoostLvL() + SpecialSigns + "/" + ImportantColor + BOLD + "3")
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Magazyn: " + ImportantColor + BOLD + ouser.getBoostChest() + SpecialSigns + "/" + ImportantColor + BOLD + "6")
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Grubasek: " + ImportantColor + BOLD + ouser.getBoostKox() + SpecialSigns + "/" + ImportantColor + BOLD + "3")
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Mniejszy Grubasek: " + ImportantColor + BOLD + ouser.getBoostRef() + SpecialSigns + "/" + ImportantColor + BOLD + "3")
					,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "UltraMoney: " + ImportantColor + BOLD + ouser.getBoostMoney() + SpecialSigns + "/" + ImportantColor + BOLD + "35")));
		final ItemBuilder c = new ItemBuilder(Material.GOLD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Ogolne" + SpecialSigns + " <<")))
					.addLores(Arrays.asList(Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Monety: " + ImportantColor + BOLD + ouser.getMoney())
								,Util.replaceString("  " + SpecialSigns + ">> " + MainColor + "Czas gry: " + ImportantColor + BOLD + GuildTimeUtil.getCzasGry(TimeUnit.SECONDS.toMillis(ouser.getTimePlay())))));
		final ItemBuilder green = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)13).setTitle("§r");
		final ItemBuilder red = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)14).setTitle("§r");
		final ItemBuilder szare = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)7).setTitle("§r");
		inv.setItem(10, a.build(),null);
		inv.setItem(12, szare.build(),null);
		inv.setItem(14, szare.build(),null);
		inv.setItem(20, szare.build(),null);
		inv.setItem(24, szare.build(),null);
		inv.setItem(28, szare.build(),null);
		inv.setItem(30, szare.build(),null);
		inv.setItem(32, szare.build(),null);
		inv.setItem(34, szare.build(),null);
		inv.setItem(40, szare.build(),null);
		inv.setItem(1, green.build(),null);
		inv.setItem(11, green.build(),null);
		inv.setItem(9, green.build(),null);
		inv.setItem(19, green.build(),null);
		inv.setItem(16, b.build(),null);
		inv.setItem(25, green.build(),null);
		inv.setItem(15, green.build(),null);
		inv.setItem(22, d.build(),null);
		inv.setItem(21, green.build(),null);
		inv.setItem(23, green.build(),null);
		inv.setItem(31, green.build(),null);
		inv.setItem(13, green.build(),null);
		inv.setItem(0, red.build(),null);
		inv.setItem(2, red.build(),null);
		inv.setItem(3, red.build(),null);
		inv.setItem(4, skull,null);
		inv.setItem(29, green.build(),null);
		inv.setItem(33, green.build(),null);
		inv.setItem(42, c.build(),null);
		inv.setItem(41, green.build(),null);
		inv.setItem(43, green.build(),null);
		inv.setItem(38, f.build(),null);
		inv.setItem(37, green.build(),null);
		inv.setItem(39, green.build(),null);
		inv.setItem(5, red.build(),null);
		inv.setItem(6, red.build(),null);
		inv.setItem(7, green.build(),null);
		inv.setItem(8, red.build(),null);
		inv.setItem(17, green.build(),null);
		inv.setItem(26, red.build(),null);
		inv.setItem(35, red.build(),null);
		inv.setItem(44, red.build(),null);
		inv.setItem(53, red.build(),null);
		inv.setItem(18, red.build(),null);
		inv.setItem(27, red.build(),null);
		inv.setItem(36, red.build(),null);
		inv.setItem(45, red.build(),null);
		inv.setItem(46, red.build(),null);
		inv.setItem(47, green.build(),null);
		inv.setItem(48, red.build(),null);
		inv.setItem(49, red.build(),null);
		inv.setItem(50, red.build(),null);
		inv.setItem(51, green.build(),null);
		inv.setItem(52, red.build(),null);
		viewer.openInventory(inv.get());
	}

}
