package pl.blackwater.guilds.inventories;

import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.guilds.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwater.guilds.ranking.*;
import pl.blackwaterapi.gui.actions.*;
import pl.blackwaterapi.utils.*;

import javax.jws.soap.SOAPBinding;
import java.util.*;

public class GuildInfoInventory implements Colors {

    public static void openMenu(@NotNull Player viewer, @NotNull Guild guild) {
        InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(MainColor + "Informacje o gildii " + ImportantColor + guild.getTag().toUpperCase())), 3);

        final ItemStack owner = ItemUtil.getPlayerHead(Bukkit.getOfflinePlayer(guild.getOwner()).getName());
        final ItemMeta itemMeta = owner.getItemMeta();
        itemMeta.setDisplayName(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Pozycja: " + ImportantColor + RankingManager.getPlaceGuild(guild))));
        final List<String> lore = Collections.singletonList(Util.fixColor(Util.replaceString(SpecialSigns + "[" + ImportantColor + guild.getTag() + SpecialSigns + "]" + ImportantColor + guild.getName())));
        itemMeta.setLore(lore);
        owner.setItemMeta(itemMeta);

        final ItemBuilder stats = new ItemBuilder(Material.DIAMOND_SWORD,1).setTitle(Util.replaceString(SpecialSigns + "* " + ImportantColor + "Statystyki: "))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Punkty: " + ImportantColor + guild.getPoints())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zabojstwa: " + ImportantColor + guild.getKills())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Smierci: " + ImportantColor + guild.getDeaths())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Dusze: " + ImportantColor + guild.getGuildSoul())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "K/D: " + ImportantColor + guild.getKD())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zycia: " + ImportantColor + guild.getLife())));

        final ItemBuilder expire = new ItemBuilder(Material.ANVIL).setTitle(Util.replaceString(SpecialSigns + "*  " + ImportantColor + "Wygasa za: "))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + ImportantColor +  Util.secondsToString((int) ((guild.getExpireTime() - System.currentTimeMillis()) / 1000L)))));

        final OfflinePlayer guildowner = Bukkit.getOfflinePlayer(guild.getOwner());
        final String preowner = guild.getPreOwner() == null ? ChatColor.DARK_RED + "Brak" : (guild.getPreOwner().isOnline() ? ImportantColor + guild.getPreOwner().getName() : ChatColor.DARK_RED + guild.getPreOwner().getName());
        final ItemBuilder members = new ItemBuilder(Material.BOOK,1).setTitle(Util.replaceString(SpecialSigns + "* " + ImportantColor + "Lista czlonkow: "))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zalozyciel: " + (guildowner.isOnline() ? ImportantColor + guildowner.getName() : ChatColor.DARK_RED + guildowner.getName()))))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zastepca: " + preowner)))
                .addLore(Util.fixColor(Util.replaceString( SpecialSigns + "  * " + MainColor + "Czlonkow: " + ImportantColor + guild.getGuildMembers().size() + "/" + guild.getPlayersLimit() + MainColor + ", online: " + ImportantColor + guild.getOnlineMembers().size())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Czlonkowie: " + getMembersToString(guild))));

        final War w = WarManager.getWar(guild);
        final ItemBuilder war = new ItemBuilder(Material.REDSTONE_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "* " + ImportantColor + "Informacje na temat wojny: ")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Wygrane wojny: " + ImportantColor + guild.getWinWars())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Zremisowane wojny: " + ImportantColor + guild.getDrawWars())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Przegrane wojny: " + ImportantColor + guild.getLoseWars())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  * " + MainColor + "Aktualnie: " + ImportantColor + (w == null ? "Brak wojny" : (w.getGuild1().equals(guild) ? "Wojna z " + w.getGuild2().getTag() : "Wojna z " + w.getGuild1().getTag())))));
        inv.setItem(4,owner,null);
        inv.setItem(18, stats.build(),null);
        inv.setItem(19,expire.build(),null);
        inv.setItem(20,members.build(),null);
        inv.setItem(21, war.build(), (player, inventory, i, itemStack) -> {
            if(w != null){
                player.closeInventory();
                WarInfoInventory.openMenu(player,w);
            }
        });
        viewer.openInventory(inv.get());
    }

    private static String getMembersToString(Guild guild){
        String[] array = new String[guild.getGuildMembers().size()];
        int i = 0;
        for(Member m : guild.getGuildMembers().values()){
            final User user = UserManager.getUser(m.getUuid());
            array[i] = (user.isOnline() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.getLastName();
            i++;
        }
        return StringUtils.join(array, MainColor + ", ");
    }

    private static String[] getAlliancesList(Guild guild){
        final Set<Alliance> alliances = AllianceManager.getGuildAlliances(guild);
        if(alliances.size() == 0){
            return new String[] {"Brak"};
        }
        String [] s = new String[alliances.size()];
        int i = 0;
        for(Alliance alliance : alliances){
            s[i] = "&6" + (alliance.getGuild1().equals(guild) ? alliance.getGuild2().getTag() : alliance.getGuild1().getTag());
            i++;
        }
        return s;
    }
}
