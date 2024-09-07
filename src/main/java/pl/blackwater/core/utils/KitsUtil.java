package pl.blackwater.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.Core;
import pl.blackwater.core.managers.EnchantManager;

public class KitsUtil
{
	@SuppressWarnings("deprecation")
	public static ItemStack getKitItem(String item)
	{
		//id-amount-data-enchant=level;enchant=level;
		final String[] array = item.split("-");
		final Material mat = Material.getMaterial(Integer.parseInt(array[0]));
        ItemStack itemStack;
        if(mat.equals(Material.BOOK)){
            final double data = Double.parseDouble(array[2]);
            itemStack = Core.getSpecialItemsManager().getDropVoucher(data);
        }else {
            final int amount = Integer.parseInt(array[1]);
            final short data = Short.parseShort(array[2]);
            itemStack = new ItemStack(mat, amount, data);
            String[] enchant;
            if (array.length >= 4) {
                enchant = array[3].split(";");
                final Map<Enchantment, Integer> enchants = new HashMap<>();
                for (String s : enchant) {
                    String[] array2 = s.split("=");
                    enchants.put(EnchantManager.get(array2[0]), Integer.valueOf(array2[1]));
                }
                itemStack.addEnchantments(enchants);
            }
        }
		return itemStack;
	}
	@SuppressWarnings("deprecation")
	public static List<ItemStack> getKitItems(List<String> items)
	{
		final List<ItemStack> itemStackList = new ArrayList<>();
		for(String item : items){
		//id-amount-data-enchant=level;enchant=level;
		final String[] array = item.split("-");
		final Material mat = Material.getMaterial(Integer.parseInt(array[0]));
		ItemStack itemStack;
		if(mat.equals(Material.BOOK)){
		    final double data = Double.parseDouble(array[2]);
		    itemStack = Core.getSpecialItemsManager().getDropVoucher(data);
		}else {
            final int amount = Integer.parseInt(array[1].replace(" ", ""));
            final short data = Short.parseShort(array[2]);
            itemStack = new ItemStack(mat, amount, data);
            String[] enchant;
            if (array.length >= 4) {
                enchant = array[3].split(";");
                final Map<Enchantment, Integer> enchants = new HashMap<>();
                for (String s : enchant) {
                    String[] array2 = s.split("=");
                    enchants.put(EnchantManager.get(array2[0]), Integer.valueOf(array2[1]));
                }
                itemStack.addEnchantments(enchants);
            }
        }
		itemStackList.add(itemStack);
		}
		return itemStackList;
	}
    public static String getDurationBreakdown(long millis) {
        if (millis == 0L) {
            return "0";
        }
        final long days = TimeUnit.MILLISECONDS.toDays(millis);
        if (days > 0L) {
            millis -= TimeUnit.DAYS.toMillis(days);
        }
        final long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if (hours > 0L) {
            millis -= TimeUnit.HOURS.toMillis(hours);
        }
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes > 0L) {
            millis -= TimeUnit.MINUTES.toMillis(minutes);
        }
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds > 0L) {
            millis -= TimeUnit.SECONDS.toMillis(seconds);
        }
        final StringBuilder sb = new StringBuilder();
        if (days > 0L) {
            sb.append(days);
            long i = days % 10L;
            if (i == 1L) {
                sb.append(" dzien ");
            }
            else {
                sb.append(" dni ");
            }
        }
        if (hours > 0L) {
            sb.append(hours);
            long i = hours % 10L;
            if (i == 1L) {
                sb.append(" godzine ");
            }
            else if (i < 4L) {
                sb.append(" godziny ");
            }
            else {
                sb.append(" godzin ");
            }
        }
        if (minutes > 0L) {
            sb.append(minutes);
            long i = minutes % 10L;
            if (i == 1L) {
                sb.append(" minute ");
            }
            else if (i < 4L) {
                sb.append(" minuty ");
            }
            else {
                sb.append(" minut ");
            }
        }
        if (seconds > 0L) {
            sb.append(seconds);
            long i = seconds % 10L;
            if (i == 1L) {
                sb.append(" sekunde");
            }
            else if (i < 5L) {
                sb.append(" sekundy");
            }
            else {
                sb.append(" sekund");
            }
        }
        return sb.toString();
    }

}
