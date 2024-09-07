package pl.blackwater.guilds.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.ItemManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.utils.ColoredMaterialsUtil;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwaterapi.gui.actions.InventoryGUI;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.MathUtil;
import pl.blackwaterapi.utils.Util;

import java.util.List;

public class GuildItemsInventory implements Colors {

    public static void openMenu(@NotNull Player player){
        final InventoryGUI inv = new InventoryGUI(Core.getPlugin(), MainColor + "Przedmioty do zalozenia " +  ImportantColor + "gildii",3);
        final ItemBuilder red = ColoredMaterialsUtil.getStainedGlassPane((short)14).setTitle(Util.fixColor(Util.replaceString(ChatColor.DARK_RED + "%X%")));
        final ItemBuilder green = ColoredMaterialsUtil.getStainedGlassPane((short)5).setTitle(Util.fixColor(Util.replaceString(ChatColor.DARK_GREEN + "%V%")));
        final List<ItemStack> itemStackList = ItemUtil.getItems((player.hasPermission("guild.vip") ? GuildConfig.COST_CREATE_VIP : GuildConfig.COST_CREATE_NORMAL), 1);
        int position = 9;
        for(ItemStack itemStack : itemStackList){
            final int amountinventory = getAmountOfItem(itemStack.getType(), player,itemStack.getDurability());
            final int amountenderchest = getAmountOfItem1(itemStack.getType(), player,itemStack.getDurability());
            final int amount = amountinventory + amountenderchest;
            final double procent = MathUtil.round((double)amount/itemStack.getAmount() * 100.0, 1);
            final ItemBuilder itemBuilder = new ItemBuilder(itemStack.getType(),1,itemStack.getDurability()).setTitle(Util.replaceString(SpecialSigns + "->> " + ChatColor.GOLD + ItemManager.get(itemStack.getType()).toUpperCase()))
                    .addLore(Util.fixColor(""))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Posiadasz &8(&2INVENTORY&8)&7: " + (amountinventory >= itemStack.getAmount() ? ChatColor.DARK_GREEN : ChatColor.DARK_RED) + amountinventory + "&7/" + ChatColor.GREEN + itemStack.getAmount())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Posiadasz &8(&2ENDERCHEST&8)&7: " + (amountenderchest >= itemStack.getAmount() ? ChatColor.DARK_GREEN : ChatColor.DARK_RED) + amountenderchest + "&7/" + ChatColor.GREEN + itemStack.getAmount())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Razem: " + (amount >= itemStack.getAmount() ? ChatColor.DARK_GREEN : ChatColor.DARK_RED) + amount + "&7/" + ChatColor.GREEN + itemStack.getAmount())))
                    .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Postep: " + (procent >= 100.0D ? ChatColor.DARK_GREEN : ChatColor.DARK_RED) + procent + "&7%")));
            inv.setItem(position - 9, (amount >= itemStack.getAmount() ? green.build() : red.build()), null);
            inv.setItem(position, itemBuilder.build(), null);
            inv.setItem(position + 9, (amount >= itemStack.getAmount() ? green.build() : red.build()), null);
            position ++;
        }
        final int amount = UserManager.getUser(player).getKills();
        final int kills = (player.hasPermission("guild.vip") ? 300 : 500);
        final double procent = MathUtil.round((double)amount/kills * 100.0, 1);
        final ItemBuilder itemBuilder = new ItemBuilder(Material.DIAMOND_SWORD).setTitle(Util.replaceString(SpecialSigns + "->> " + ChatColor.GOLD + "ZABOJSTWA"))
                .addLore(Util.fixColor(""))
                .addLore(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Posiadasz: " + (amount >= kills ? ChatColor.DARK_GREEN : ChatColor.DARK_RED) + amount + "&7/" + ChatColor.GREEN + kills))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + " ->> " + MainColor + "Postep: " + (procent >= 100.0D ? ChatColor.DARK_GREEN : ChatColor.DARK_RED) + procent + "&7%")));
        inv.setItem(17 - 9, (amount >= kills ? green.build() : red.build()), null);
        inv.setItem(17, itemBuilder.build(), null);
        inv.setItem(17 + 9, (amount >= kills ? green.build() : red.build()), null);
        player.openInventory(inv.get());
    }

    private static int getAmountOfItem(Material type, Player p, short data){
        int amount = 0;
        for(ItemStack is : p.getInventory().getContents()){
            if(is == null) continue;
            if(is.getType().equals(type) && is.getDurability() == data) amount += is.getAmount();
        }
        return amount;
    }

    private static int getAmountOfItem1(Material type, Player p, short data){
        int amount = 0;
        for(ItemStack is : p.getEnderChest().getContents()){
            if(is == null) continue;
            if(is.getType().equals(type) && is.getDurability() == data) amount += is.getAmount();
        }
        return amount;
    }
}
