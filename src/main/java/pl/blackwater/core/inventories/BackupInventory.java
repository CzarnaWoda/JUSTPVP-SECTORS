package pl.blackwater.core.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.core.commands.BackupCommand;
import pl.blackwater.core.data.Backup;
import pl.blackwater.core.enums.BackupType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.backups.BackupCreatePacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.Arrays;
import java.util.List;

public class BackupInventory implements Colors {
    public static void BackupInventory(Player p){
        InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "BACKUP " + MainColor + "MENU " + SpecialSigns + "<<-")), 3);
        ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)1).setTitle("§2");

        for(int i = 0; i < inv.get().getSize(); i ++){
            inv.setItem(i,air.build(),null);
        }
        ItemBuilder search = new ItemBuilder(Material.BOOK_AND_QUILL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Szukaj zapisow " + ImportantColor + BOLD + "GRACZA")));
        ItemBuilder globalsave = new ItemBuilder(Material.TRIPWIRE_HOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Wykonaj zapis " + ImportantColor + BOLD + "GLOBALNY")));
        ItemBuilder info = new ItemBuilder(Material.BOOK).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ImportantColor + BOLD + "INFORMACJE")));
        inv.setItem(10, search.build(), (player, inventory, i, itemStack) -> {
            BackupCommand.getFindPlayer().add(player);
            ActionBarUtil.sendActionBar(player,Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Napisz na chacie nick " + ImportantColor + "gracza")));
            player.closeInventory();
        });
        inv.setItem(13, globalsave.build(), (player, inventory, i, itemStack) -> {
            Util.sendMsg(player, Util.replaceString(ImportantColor + "&nBACKUPMANAGER" + SpecialSigns + " ->> " + MainColor + "Trwa tworzenie zapisu globalnego wszystkich " + ImportantColor + "graczy"));
            for(Player online : Bukkit.getOnlinePlayers()){
                Util.sendMsg(online, Util.replaceString(ImportantColor + "&nBACKUPMANAGER" + SpecialSigns + " ->> " + MainColor + "Trwa tworzenie zapisu twojego " + ImportantColor + "ekwipunku" + MainColor + "..."));
                final Backup backup = Core.getBackupManager().createBackup(online, BackupType.GLOBAL, player.getName());
                backup.insert();
                RedisClient.sendSectorsPacket(new BackupCreatePacket(backup));
                Util.sendMsg(online, Util.replaceString(ImportantColor + "&nBACKUPMANAGER" + SpecialSigns + " ->> " + MainColor + "Stworzono zapis twojego " + ImportantColor + "ekwipunku"));
            }
            Util.sendMsg(player, Util.replaceString(ImportantColor + "&nBACKUPMANAGER" + SpecialSigns + " ->> " + MainColor + "Stworzono zapis wszystkich " + ImportantColor + "graczy"));
            player.closeInventory();
        });
        inv.setItem(16, info.build(),null);
        inv.openInventory(p);
    }
    public static void openPlayerMenu(Player player, Player other){
        InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString("->> " + MainColor + "BACKUPS " + SpecialSigns + " ->> " + ImportantColor + BOLD  + other.getName().toUpperCase())), 6);
        ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)10).setTitle("§2");
        List<Integer> list = Arrays.asList(0,2,4,6,8,9,11,13,15,17,18,20,22,24,26,27,29,31,33,35,36,38,40,42,44,45,47,49,51,53);
        for(int i : list){
            inv.setItem(i,air.build(),null);
        }
        List<Backup> manuals = Core.getBackupManager().getBackups(other.getUniqueId(), BackupType.MANUAL);
        ItemBuilder manual = new ItemBuilder(Material.STAINED_CLAY,1,(short)5).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "BACKUP: " + ImportantColor + UnderLined + "MANUAL")))
                .addLore(Util.fixColor(Util.replaceString( SpecialSigns + "  ->> " + MainColor + "Liczba zapisow: " + ImportantColor + UnderLined + manuals.size())));
        List<Backup> globals = Core.getBackupManager().getBackups(other.getUniqueId(), BackupType.GLOBAL);
        ItemBuilder global = new ItemBuilder(Material.STAINED_CLAY,1,(short)4).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "BACKUP: " + ImportantColor + UnderLined + "GLOBAL")))
                .addLore(Util.fixColor(Util.replaceString( SpecialSigns + "  ->> " + MainColor + "Liczba zapisow: " + ImportantColor + UnderLined + globals.size())));
        List<Backup> automatics = Core.getBackupManager().getBackups(other.getUniqueId(), BackupType.AUTOMATIC);
        ItemBuilder automatic = new ItemBuilder(Material.STAINED_CLAY,1,(short)1).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "BACKUP: " + ImportantColor + UnderLined + "AUTOMATIC")))
                .addLore(Util.fixColor(Util.replaceString( SpecialSigns + "  ->> " + MainColor + "Liczba zapisow: " + ImportantColor + UnderLined + automatics.size())));
        List<Backup> closeservers = Core.getBackupManager().getBackups(other.getUniqueId(), BackupType.CLOSESERVER);
        ItemBuilder closeserver = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "BACKUP: " + ImportantColor + UnderLined + "CLOSESERVER")))
                .addLore(Util.fixColor(Util.replaceString( SpecialSigns + "  ->> " + MainColor + "Liczba zapisow: " + ImportantColor + UnderLined + closeservers.size())));
        inv.setItem(1,manual.build(),null);
        inv.setItem(3,global.build(),null);
        inv.setItem(5,automatic.build(),null);
        inv.setItem(7,closeserver.build(),null);
        int index = 10;
        for(Backup b : manuals){
            ItemBuilder manualBackup = new ItemBuilder(Material.BOOK_AND_QUILL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ImportantColor + UnderLined + "BACKUP" + SpecialSigns + " * " + MainColor + b.getBackupUUID().toString())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wlasciciel: " + ImportantColor + UnderLined + other.getName())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Czas stworzenia: " + ImportantColor + UnderLined + Util.getDate(b.getBackupTime()))))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Status: " + (b.getTakeBackupTime() == 0L ? ChatColor.GREEN + "%V%, Nie przywracany" : ChatColor.DARK_RED + "%X%, Przywracany: " + Util.getDate(b.getTakeBackupTime())))))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Stworzony przez: " + ImportantColor + UnderLined + b.getBackupCreator())));
            inv.setItem(index, manualBackup.build(), (p, inventory, i, itemStack) -> {
                p.closeInventory();
                openBackup(p,b);
            });
            index+=9;
        }
        int index1 = 12;
        for(Backup b : globals){
            ItemBuilder globalBackup = new ItemBuilder(Material.BOOK_AND_QUILL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ImportantColor + UnderLined + "BACKUP" + SpecialSigns + " * " + MainColor + b.getBackupUUID().toString())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wlasciciel: " + ImportantColor + UnderLined + other.getName())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Czas stworzenia: " + ImportantColor + UnderLined + Util.getDate(b.getBackupTime()))))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Status: " + (b.getTakeBackupTime() == 0L ? ChatColor.GREEN + "%V%, Nie przywracany" : ChatColor.DARK_RED + "%X%, Przywracany: " + Util.getDate(b.getTakeBackupTime())))))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Stworzony przez: " + ImportantColor + UnderLined + b.getBackupCreator())));
            inv.setItem(index1, globalBackup.build(), (p, inventory, i, itemStack) -> {
                p.closeInventory();
                openBackup(p,b);
            });
            index1+=9;
        }
        int index2 = 14;
        for(Backup b : automatics){
            ItemBuilder automaticBackup = new ItemBuilder(Material.BOOK_AND_QUILL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ImportantColor + UnderLined + "BACKUP" + SpecialSigns + " * " + MainColor + b.getBackupUUID().toString())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wlasciciel: " + ImportantColor + UnderLined + other.getName())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Czas stworzenia: " + ImportantColor + UnderLined + Util.getDate(b.getBackupTime()))))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Status: " + (b.getTakeBackupTime() == 0L ? ChatColor.GREEN + "%V%, Nie przywracany" : ChatColor.DARK_RED + "%X%, Przywracany: " + Util.getDate(b.getTakeBackupTime())))))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Stworzony przez: " + ImportantColor + UnderLined + b.getBackupCreator())));
            inv.setItem(index2, automaticBackup.build(), (p, inventory, i, itemStack) -> {
                p.closeInventory();
                openBackup(p,b);
            });
            index2+=9;
        }
        int index3 = 16;
        for(Backup b : closeservers){
            ItemBuilder closeserverBackup = new ItemBuilder(Material.BOOK_AND_QUILL).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ImportantColor + UnderLined + "BACKUP" + SpecialSigns + " * " + MainColor + b.getBackupUUID().toString())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wlasciciel: " + ImportantColor + UnderLined + other.getName())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Czas stworzenia: " + ImportantColor + UnderLined + Util.getDate(b.getBackupTime()))))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Status: " + (b.getTakeBackupTime() == 0L ? ChatColor.GREEN + "%V%, Nie przywracany" : ChatColor.DARK_RED + "%X%, Przywracany: " + Util.getDate(b.getTakeBackupTime())))))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Stworzony przez: " + ImportantColor + UnderLined + b.getBackupCreator())));
            inv.setItem(index3, closeserverBackup.build(), (p, inventory, i, itemStack) -> {
                p.closeInventory();
                openBackup(p,b);
            });
            index3+=9;
        }
        inv.openInventory(player);
    }
    public static void openBackup(Player player, Backup backup){
        InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString("->> " + MainColor + "BACKUPS " + SpecialSigns + " ->> " + ImportantColor + BOLD  + backup.getBackupUUID().toString())), 6);
        ItemBuilder air = new ItemBuilder(Material.STAINED_GLASS_PANE,1,(short)2).setTitle("§2");
        List<Integer> list = Arrays.asList(36,37,38,39,40,41,42,43,44,47,52);
        for(int i : list){
            inv.setItem(i,air.build(),null);
        }
        int index = 35;
        for(ItemStack itemStack : backup.getInventory()){
            inv.setItem(index, itemStack,null);
            index--;
        }
        int index1 = 51;
        for(ItemStack itemStack : backup.getArmor()){
            inv.setItem(index1, itemStack, null);
            index1--;
        }
        ItemBuilder returnBackup = new ItemBuilder(Material.STAINED_CLAY,1,(short)13).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ChatColor.BLUE + "Przywroc " + MainColor + "zapis")));
        ItemBuilder back = new ItemBuilder(Material.STAINED_CLAY,1,(short)11).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ChatColor.LIGHT_PURPLE + "Powrot do zapisow " + MainColor + "gracza")));
        ItemBuilder removeBackup = new ItemBuilder(Material.STAINED_CLAY,1,(short)14).setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + ChatColor.DARK_RED + "Usun " + MainColor + "zapis")));
        inv.setItem(45,returnBackup.build(), (p, inventory, i, itemStack) -> {
            Player o = Bukkit.getPlayer(backup.getOwner());
            o.getInventory().setArmorContents(backup.getArmor());
            o.getInventory().setContents(backup.getInventory());
            backup.setTakeBackupTime(System.currentTimeMillis());
            backup.update(true);
            ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Przywrocono" + ImportantColor + " zapis")));
            ActionBarUtil.sendActionBar(o, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Przywrocono" + ImportantColor + " zapis")));
            p.closeInventory();
            openPlayerMenu(p, o);
        });
        inv.setItem(46, back.build(), (p, inventory, i, itemStack) -> {
            p.closeInventory();
            openPlayerMenu(p, Bukkit.getPlayer(backup.getOwner()));
        });
        inv.setItem(53, removeBackup.build(), (p, inventory, i, itemStack) -> {
            Core.getBackupManager().deleteBackup(backup);
            ActionBarUtil.sendActionBar(p, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Usunieto" + ImportantColor + " zapis")));
            openPlayerMenu(p, Bukkit.getPlayer(backup.getOwner()));
        });
        inv.openInventory(player);
    }
}
