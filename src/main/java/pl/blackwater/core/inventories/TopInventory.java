package pl.blackwater.core.inventories;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.utils.ColoredMaterialsUtil;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.ranking.RankingManager;
import pl.blackwater.guilds.utils.GuildTimeUtil;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;

public class TopInventory implements Colors{
	
	public static InventoryView openMenu(Player player) {
		final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + ">> " + ImportantColor + "Top Menu" + SpecialSigns + " <<")), 3);
		final ItemBuilder blue = ColoredMaterialsUtil.getStainedGlassPane((short)11);
		for(int i = 0; i < 27; i ++) 
			inv.setItem(i, blue.build(), null);
		final ItemBuilder topkills = getTopListUsers(RankingManager.getRankings(), "zabojstw", Material.DIAMOND_SWORD, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "ZABOJSTW")));
		inv.setItem(1, topkills.build(), null);
		final ItemBuilder topguilds = new ItemBuilder(Material.DRAGON_EGG).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "GILDII")));
		final List<Guild> guildList = RankingManager.getGuildRankings();
		for(int i = 0; i < 10; i ++) {
			if(i >= guildList.size())
				topguilds.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  " + (i+1) + " * " + MainColor + "Brak")));
			else {
				final Guild g = guildList.get(i);
				topguilds.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  " + (i+1) + " * " + MainColor + g.getTag() + SpecialSigns + " >> " + ImportantColor + g.getPoints() + MainColor + " pkt")));
			}
		}
		inv.setItem(9, topguilds.build(), null);
		final ItemBuilder topstone = getTopListTopUsers(RankingManager.getRankingsByStoneBreak(), "kamienia", Material.STONE, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "WYKOPANEGO KAMIENIA")));
		inv.setItem(11, topstone.build(), null);
		final ItemBuilder topkillstreak = getTopListTopUsers(RankingManager.getRankingsByKillStreak(), "killstreak", Material.DIAMOND_AXE, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "KILLSTREAK")));
		inv.setItem(19, topkillstreak.build(), null);
		final ItemBuilder topmoney = getTopListTopUsers(RankingManager.getRankingsByCoins(), "monet", Material.GOLD_INGOT, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "MONET")));
		inv.setItem(4, topmoney.build(), null);
		final ItemBuilder topopenchest = getTopListTopUsers(RankingManager.getRankingsByOpenPremiumChest(), "skrzynek", Material.TRIPWIRE_HOOK, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "OTWORZONYCH SKRZYNEK")));
		inv.setItem(13, topopenchest.build(), null);
		final ItemBuilder toptimeplay = getTopListTopUsers(RankingManager.getRankingsByTimePlay(), "czasu", Material.WATCH, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "ONLINE")));
		inv.setItem(22, toptimeplay.build(), null);
		final ItemBuilder toplevel = getTopListTopUsers(RankingManager.getRankingsByLevel(), "lvl", Material.EXP_BOTTLE, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "LEVEL'U")));
		inv.setItem(7, toplevel.build(), null);
		final ItemBuilder topref = getTopListTopUsers(RankingManager.getRankingsByEatRef(), "refili", Material.GOLDEN_APPLE, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "ZJEDZONYCH REFILI")));
		inv.setItem(15, topref.build(), null);
		final ItemBuilder topkox = getTopListTopUsers(RankingManager.getRankingsByEatKox(), "koxow", Material.GOLDEN_APPLE, (short)1, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "ZJEDZONYCH KOXOW")));
		inv.setItem(17, topkox.build(), null);
		final ItemBuilder topdeath = getTopListTopUsers(RankingManager.getRankingsByDeaths(), "smierci", Material.BARRIER, (short)0, Util.fixColor(Util.replaceString(SpecialSigns + "* " + MainColor + "Top " + ImportantColor + "SMIERCI")));
		inv.setItem(25, topdeath.build(), null);
		return player.openInventory(inv.get());
		
	}
	
	private static ItemBuilder getTopListUsers(final List<User> users, final String type,final Material material,final short data, final String title) {
		final ItemBuilder top = new ItemBuilder(material,1,data).setTitle(title);
		for(int i = 0; i < 10; i ++) {
			if(i >= users.size()) 
				top.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  " + (i+1) + " * " + MainColor + "Brak")));
			else {
				final User u = users.get(i);
				final int value = (type.equals("zabojstw") ? u.getKills() : (type.equals("kamienia") ? u.getDropStones() : u.getDeaths()));
				top.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  " + (i+1) + " * " + MainColor + u.getLastName() + SpecialSigns + " >> " + ImportantColor + value + MainColor + " " + type)));
			}
		}
		return top;
	}
	private static ItemBuilder getTopListTopUsers(final List<User> users, final String type,final Material material,final short data, final String title) {
		final ItemBuilder top = new ItemBuilder(material,1,data).setTitle(title);
		for(int i = 0; i < 10; i ++) {
			if(i >= users.size()) 
				top.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  " + (i+1) + " * " + MainColor + "Brak")));
			else {
				final User u = users.get(i);
				final int value = (type.equals("zabojstw") ? u.getKills() : (type.equals("kamienia") ? u.getDropStones(): (type.equals("killstreak") ? u.getKillStreak() : (type.equals("monet") ? u.getMoney() : (type.equals("skrzynek") ? u.getOpenPremiumChest() : (type.equals("czasu") ? u.getTimePlay() : (type.equals("refili") ? u.getEatRef() : (type.equals("lvl") ? u.getLevel() : (type.equals("koxow") ? u.getEatKox() : (type.equals("smierci") ? u.getDeaths() : -1000))))))))));
				top.addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  " + (i+1) + " * " + MainColor + u.getLastName() + SpecialSigns + " >> " + ImportantColor + (!type.equals("czasu") ? value : GuildTimeUtil.getCzasGry(TimeUnit.SECONDS.toMillis(u.getTimePlay()))) + MainColor + " " + (type.equals("czasu") ? "" : type))));
			}
		}
		return top;
	}

}
