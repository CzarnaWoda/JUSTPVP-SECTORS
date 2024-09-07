package pl.blackwater.core.inventories;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Combat;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.CombatManager;
import pl.blackwater.core.managers.TeleportManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;

import java.util.LinkedList;
import java.util.List;

public class ChannelInventory implements Colors {

    final static ItemBuilder black = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 1).setTitle(Util.fixColor("&6#"));

    public static void openInventory(Player player) {
        final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), Util.fixColor(Util.replaceString("&8|->> [ " + MainColor + "LISTA " + ImportantColor + UnderLined + "SEKTOROW" + SpecialSigns + " ] <<-|")), 3);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, black.build(), null);
        }
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, black.build(), null);
        }

        for (Sector sector : SectorManager.getSectors().values()) {
            if(sector.getSectorType() != SectorType.SPAWN) continue;
            final ItemBuilder a = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 11).setTitle(Util.replaceString("&8>>->> ( " + ImportantColor + BOLD  + sector.getSectorName().toUpperCase() + " &8) <<-<<"))
                    .addLore("")
                    .addLore(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Informacje " +  SpecialSigns + "&8*" + ImportantColor + " SEKTOR"))
                    .addLore("");
                    if(player.hasPermission("core.channel.bypass")) {
                        a.addLore(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Online: " + ImportantColor + sector.getOnlinePlayers().size()));
                    }
                    a.addLore("")
                    .addLore(MainColor + "KLIKNIJ ABY SIE " + ImportantColor + "&nPRZENIESC" + MainColor + "!");
            inv.addItem(a.build(), ((invPlayer, paramInventory, paramInt, paramItemStack) -> {
                final User user = UserManager.getUser(invPlayer.getUniqueId());
                if(user == null)
                {
                    player.kickPlayer("Blad! zglos administratorowi!");
                    return;
                }
                if(!sector.isOnline())
                {
                    Util.sendMsg(player, "&7Sektor (&a" + sector.getSectorName() + "&7) jest obecnie wylaczony!");
                    return;
                }
                final Combat combat = CombatManager.getCombat(player);
                if(combat != null && combat.hasFight())
                {
                    Util.sendMsg(player, "&4Blad! &cNie mozesz zmienic sektora podczas walki!");
                    return;
                }
                final Sector currentSector = SectorManager.getCurrentSector().get();
                if(player.hasPermission("core.channel.bypass") || currentSector.getSectorType() == SectorType.MINE || currentSector.getSectorType() == SectorType.STORAGE || ((currentSector.getOnlinePlayers().size() > 390 && sector.getOnlinePlayers().size() < 390 && sector.getTps() > 19.5)
                        || sector.getOnlinePlayers().size() > 30)
                ) {
                    TeleportManager.teleportToSpawn(user, user.asPlayer(), sector, false);
                    player.closeInventory();
                }
                else
                {
                    player.sendMessage(Util.fixColor("&4Na serwerze musi znajdowac sie wiecej graczy aby przejsc na ten sektor!"));
                }
            }));
        }
        inv.openInventory(player);
    }
}
