package pl.blackwater.core.managers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.utils.ItemBuilder;
import pl.blackwaterapi.utils.Util;

public class SpecialItemsManager implements Colors {

    public ItemStack getDropVoucher(double addDrop){
        final ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK)
                .setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Voucher na " + ImportantColor + "DROP")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dodaje % dropu dla kazdego itemu pod " + ImportantColor + "/drop z stone")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dodaje: " + ImportantColor + addDrop)))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dziala" + ImportantColor + " na zawsze"  +  MainColor + "!")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Kliknij " + ImportantColor + "PPM " + MainColor + "aby uzyc!")));
        itemBuilder.addEnchantment(Enchantment.DIG_SPEED, 10);
        return itemBuilder.build();
    }
    public ItemStack getRankVoucher(Rank rank,String time){
        final ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK)
                .setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Voucher na " + ImportantColor + "RANGE")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher nadaje daną range " + ImportantColor + "na " + time.toLowerCase())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher nadaje: " + ImportantColor + rank.getName().toUpperCase())))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dziala" + ImportantColor + " jednorazowo"  +  MainColor + "!")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Kliknij " + ImportantColor + "PPM " + MainColor + "aby uzyc!")));
        itemBuilder.addEnchantment(Enchantment.LURE, 10);
        return itemBuilder.build();
    }
    public ItemStack getMoneyVoucher(int money){
        final ItemBuilder itemBuilder = new ItemBuilder(Material.PAPER)
                .setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Voucher na " + ImportantColor + "MONETY")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dodaje monety, stan konta pod " + ImportantColor + "/money")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dodaje: " + ImportantColor + money)))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Voucher dziala" + ImportantColor + " jednorazowo"  +  MainColor + "!")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Kliknij " + ImportantColor + "PPM " + MainColor + "aby uzyc!")));
        itemBuilder.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
        return itemBuilder.build();
    }
    public ItemStack getServerVault(int amount){
        final ItemBuilder itemBuilder = new ItemBuilder(Material.NETHER_STAR,amount)
                .setTitle(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Just" + ImportantColor + "COIN")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Jest to specjalna " + ImportantColor + "waluta serwerowa")))
                .addLore(Util.fixColor(Util.replaceString(SpecialSigns + "  ->> " + MainColor + "Wymienic ją można pod " + ImportantColor + "/sklep")));
        return itemBuilder.build();
    }
    public ItemStack getPumpkin(int amount){
        final ItemBuilder pumpkin = new ItemBuilder(Material.PUMPKIN,amount).setTitle(Util.fixColor(Util.replaceString("&8->> &c&lMAGICZNA DYNIA &8<<-")))
                .addLore(Util.fixColor(Util.replaceString("  &8->> &6&lHALLOWEEN")));
        pumpkin.addEnchantment(Enchantment.DIG_SPEED,2);
        return pumpkin.build();
    }
    public boolean checkRankVoucher(ItemStack voucher){
        if(voucher.getType().equals(Material.BOOK)){
            if(voucher.getItemMeta() != null && voucher.getItemMeta().getDisplayName().equalsIgnoreCase(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Voucher na " + ImportantColor + "RANGE")))){
                return voucher.getEnchantments().containsKey(Enchantment.LURE);
            }
        }
        return false;
    }
    public boolean checkDropVoucher(ItemStack voucher){
        if(voucher.getType().equals(Material.BOOK)){
            if(voucher.getItemMeta() != null && voucher.getItemMeta().getDisplayName().equalsIgnoreCase(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Voucher na " + ImportantColor + "DROP")))){
                return voucher.getEnchantments().containsKey(Enchantment.DIG_SPEED);
            }
        }
        return false;
    }
    public boolean checkMoneyVoucher(ItemStack voucher){
        if(voucher.getType().equals(Material.PAPER)){
            if(voucher.getItemMeta() != null && voucher.getItemMeta().getDisplayName().equalsIgnoreCase(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Voucher na " + ImportantColor + "MONETY")))){
                return voucher.getEnchantments().containsKey(Enchantment.LOOT_BONUS_MOBS);
            }
        }
        return false;
    }
    public boolean checkServerVault(ItemStack serverVault){
        if(serverVault.getType().equals(Material.NETHER_STAR)){
            if(serverVault.getItemMeta() != null && serverVault.getItemMeta().getDisplayName().equalsIgnoreCase(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + MainColor + "Just" + ImportantColor + "COIN")))){
                return serverVault.getEnchantments().containsKey(Enchantment.DIG_SPEED);
            }
        }
        return false;
    }
    public boolean checkAndRemove(Player player,ItemStack serverVault){
        if(player.getInventory().containsAtLeast(serverVault, serverVault.getAmount())){
            player.getInventory().removeItem(serverVault);
            return true;
        }
        return false;
    }
}
