package pl.blackwater.core.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.utils.ColoredMaterialsUtil;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;

public class ServerInventory implements Colors {
    public static void ServerInventory(Player player){
        final InventoryGUI inventoryGUI = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje o" + ImportantColor + " serwerze")),3);
        final ItemBuilder air = ColoredMaterialsUtil.getStainedGlassPane((short)6);
        for(int i = 0 ; i < inventoryGUI.get().getSize(); i ++){
            inventoryGUI.setItem(i,air.build(),null);
        }
        final ItemBuilder sets = new ItemBuilder(Material.ENCHANTMENT_TABLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje o " + ImportantColor + "enchantach")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Enchanty na setach: " + ImportantColor + "Protection 4 " + SpecialSigns + " * " + ImportantColor + "Unbreaking 3")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Enchanty na mieczach: " + ImportantColor + "Sharpness 4 " + SpecialSigns + " * " + ImportantColor + "Fire Aspect 1")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Enchanty na lukach: " + ImportantColor + "Power 3 " + SpecialSigns + " * " + ImportantColor + "Infinity I")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Informacje ogólnie: " + ImportantColor + "Enchanty pod /sklep są zmniejszane wraz z zabojstwami na mapie")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Informacje ogólnie: " + ImportantColor + "Zwykly enchant zabiera normalnie poziomy np 3 lvl na full enchancie")));
        final ItemBuilder limits = new ItemBuilder(Material.GOLDEN_APPLE).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje o " + ImportantColor + "limitach")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Limit koxow: " + ImportantColor + CoreConfig.LIMITMANAGER_KOX)))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Limit refow: " + ImportantColor + CoreConfig.LIMITMANAGER_REF)))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Limit perel: " + ImportantColor + CoreConfig.LIMITMANAGER_PEARL)));
        final ItemBuilder blocks = new ItemBuilder(Material.BARRIER).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje o " + ImportantColor + "blokadach")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Bugowanie: " + ImportantColor + "OFF")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Punch: " + ImportantColor + "OFF")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Perly: " + ImportantColor + "ON")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Diamentowe itemy: " + ImportantColor + "OFF")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Sojusze: " + ImportantColor + "OFF")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Limit graczy w gildii: " + ImportantColor + "10 osob")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "BlazingPack: " + ImportantColor + "ON")));
        final ItemBuilder wars = new ItemBuilder(Material.DRAGON_EGG).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje o " + ImportantColor + "wojnach")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wojne wypowiadamy drugiej gildi za pomoca komendy: " + ImportantColor + "/g wojna")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Przygotowanie do wyjny trwa: " + ImportantColor + "10 minut")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wojna trwa: " + ImportantColor + "1 godzine")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wojne wygrywa gildia ktora zabila najwiecej graczy: " + ImportantColor + "przeciwnej gildii")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Ranking gildii jest uzalezniony tylko iwylacznie: " + ImportantColor + "od wynikow wojen")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Aby sprawdzic status oraz wynik wojny gildii X z gildia Y " + ImportantColor + "/g sprawdzwojne X Y")));
        final ItemBuilder chests = new ItemBuilder(Material.CHEST).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje o " + ImportantColor + "skrzynkach")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wiekszosc skrzynek na mapie: " + ImportantColor + "jest randomowa")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Skrzynki z najwazniejszymi itemami sa: " + ImportantColor + "stale")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Drop z skrzynek na calej mapie jest: " + ImportantColor + "zalezy od zabojstw gracza")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Czym wiecej masz zabojstw tym: " + ImportantColor + "wiecej itemow otrzymasz")));
        final ItemBuilder youtube = new ItemBuilder(Material.BEACON).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje o " + ImportantColor + "randze YouTube")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Range youtube mozna automatycznie uzyskac: " + ImportantColor + "pod /youtube")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Aby uzyskac range trzeba miec minimum: " + ImportantColor + "1000 subskrybcji")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Aby podjac stala wspolprace: " + ImportantColor + "nalezy wejsc na teamspeak")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Proszenie o donejty: " + ImportantColor + "zabronione")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Ranga YOUTUBE posiada uprawnienia rangi: " + ImportantColor + "VIP")));
        final ItemBuilder main = new ItemBuilder(Material.EMERALD_BLOCK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje " + ImportantColor + "ogolnie")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "FanPage: " + ImportantColor + "fb.com/justpvppl")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Discord: " + ImportantColor + "justpvp.discord.pl")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "TeamSpeak: " + ImportantColor + "just24.pl")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Strona WWW/ItemShop: " + ImportantColor + "justpvp.pl")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Zarzadzanie chatem: " + ImportantColor + "/cc")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Osiagniecia (trzeba odebrac): " + ImportantColor + "/os")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Rangi premium: " + ImportantColor + "VIP SVIP")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Nagroda za polubienie fanpage: " + ImportantColor + "/fanpage")));

        inventoryGUI.setItem(10,sets.build(),null);
        inventoryGUI.setItem(11,limits.build(),null);
        inventoryGUI.setItem(12,blocks.build(), null);
        inventoryGUI.setItem(13,wars.build(), null);
        inventoryGUI.setItem(14,chests.build(), null);
        inventoryGUI.setItem(15,youtube.build(), null);
        inventoryGUI.setItem(16,main.build(), null);
        inventoryGUI.openInventory(player);
    }
}
