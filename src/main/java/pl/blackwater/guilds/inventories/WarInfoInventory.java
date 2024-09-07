package pl.blackwater.guilds.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.utils.ColoredMaterialsUtil;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.War;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;

public class WarInfoInventory implements Colors {

    public static void openMenu(Player player, War war){
        final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje na temat " + ImportantColor + "wojny")),3);

        final User user = UserManager.getUser(player);
        final Guild guild = user.getGuild();
        
        final Guild g1 = war.getGuild1();
        final Guild g2 = war.getGuild2();
        final int kills1 = war.getKills1();
        final  int kills2 = war.getKills2();
        
        final ItemBuilder guild1 = new ItemBuilder(Material.STAINED_CLAY,1,(short)(guild == null ? 0 : (g1.equals(guild) ? 11 : 14))).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + SpecialSigns + "[" + ImportantColor + g1.getTag() + SpecialSigns + "]" + ImportantColor + g1.getName())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Punkty: " + ImportantColor + g1.getPoints())));
        final ItemBuilder win1 = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wygrane wojny: " + ImportantColor + g1.getWinWars())));
        final ItemBuilder draw1 = new ItemBuilder(Material.GOLD_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Zremisowane wojny: " + ImportantColor + g1.getDrawWars())));
        final ItemBuilder lose1 = new ItemBuilder(Material.WOOD_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Przegrane wojny: " + ImportantColor + g1.getLoseWars())));

        final ItemBuilder guild2 = new ItemBuilder(Material.STAINED_CLAY,1,(short)(guild == null ? 0 : (g2.equals(guild) ? 11 : 14))).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + SpecialSigns + "[" + ImportantColor + g2.getTag() + SpecialSigns + "]" + ImportantColor + g2.getName())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Punkty: " + ImportantColor + g2.getPoints())));
        final ItemBuilder win2 = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wygrane wojny: " + ImportantColor + g2.getWinWars())));
        final ItemBuilder draw2 = new ItemBuilder(Material.GOLD_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Zremisowane wojny: " + ImportantColor + g2.getDrawWars())));
        final ItemBuilder lose2 = new ItemBuilder(Material.WOOD_SWORD).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Przegrane wojny: " + ImportantColor + g2.getLoseWars())));

        final ItemBuilder score = new ItemBuilder(Material.REDSTONE_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wynik wojny")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + ImportantColor + g1.getTag() + SpecialSigns + " >> " + MainColor + "zabojstwa: " + ImportantColor + kills1 + SpecialSigns + " >> " + MainColor + "smierci: " + ImportantColor + kills2)))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + ImportantColor + g2.getTag() + SpecialSigns + " >> " + MainColor + "zabojstwa: " + ImportantColor + kills2 + SpecialSigns + " >> " + MainColor + "smierci: " + ImportantColor + kills1)))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Wygrywa: " + ImportantColor + (kills1 > kills2 ? g1.getTag() : (kills1 == kills2 ? "REMIS" : g2.getTag())))))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Status: " + ImportantColor + (war.isStart() ? "TRWA" : "Zacznie sie za " + Util.secondsToString((int) ((war.getStartTime() - System.currentTimeMillis()) / 1000))))))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " * " + MainColor + "Koniec wojny za: " + ImportantColor + Util.secondsToString((int) ((war.getEndTime() - System.currentTimeMillis())/1000)))));
        final ItemBuilder air = ColoredMaterialsUtil.getStainedGlassPane((short)7);

        inv.setItem(1, guild1.build(),null);
        inv.setItem(3, air.build(),null);
        inv.setItem(5, air.build(),null);
        inv.setItem(7, guild2.build(),null);
        inv.setItem(12, air.build(),null);
        inv.setItem(13, score.build(),null);
        inv.setItem(14, air.build(),null);
        inv.setItem(18, win1.build(),null);
        inv.setItem(19, draw1.build(),null);
        inv.setItem(20, lose1.build(),null);
        inv.setItem(21, air.build(),null);
        inv.setItem(23, air.build(),null);
        inv.setItem(24, win2.build(),null);
        inv.setItem(25, draw2.build(),null);
        inv.setItem(26, lose2.build(),null);
        inv.openInventory(player);
    }
}
