package pl.blackwater.core.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.EnchantManager;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class GiveCommand extends Command implements Colors
{
    public GiveCommand() {
        super("give", "dawanie przemiotow graczom", "/give <gracz> <id[:base]> [ilosc] [zaklecia...]", "core.give", "giveitem");
    }
    
    @SuppressWarnings({"rawtypes" })
	public boolean onExecute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
        Player p = Bukkit.getPlayer(args[0]);
        String[] datas = args[1].split(":");
        Material m = Util.getMaterial(datas[0]);
        short data = ((datas.length > 1) ? Short.parseShort(datas[1]) : 0);
        if (p == null) {
            return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        }
        if (m == null) {
            return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Nazwa lub ID przedmiotu jest bledne!");
        }
        ItemStack item = null;
        if (args.length == 2) {
            item = Util.getItemStack(m, data, 1, null);
        }
        else if (args.length == 3) {
            item = Util.getItemStack(m, data, Util.isInteger(args[2]) ? Integer.parseInt(args[2]) : 1, null);
        }
        else {
            HashMap<Enchantment, Integer> enchants = new HashMap<>();
            for (int i = 3; i < args.length; ++i) {
                String[] nameAndLevel = args[i].split(":");
                Enchantment e = EnchantManager.get(nameAndLevel[0]);
                int level = Util.isInteger(nameAndLevel[1]) ? Integer.parseInt(nameAndLevel[1]) : 1;
                enchants.put(e, level);
            }
            item = Util.getItemStack(m, data, Util.isInteger(args[2]) ? Integer.parseInt(args[2]) : 1, enchants);
        }
        if(sender.hasPermission("core.admin.creative")){
        Map<Player, Integer> amountget = new HashMap<>();
        ItemMeta im = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        amountget.put(p, item.getAmount());
        SimpleDateFormat d = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        lore.add(ChatColor.DARK_RED + "ITEM OD ADMINA");
        lore.add(" ");
        lore.add(ChatColor.GOLD + "ADMIN" + ChatColor.GRAY + ": " + sender.getName());
        lore.add(ChatColor.GOLD + "ITEM" + ChatColor.GRAY + ": " + item.getType());
        lore.add(ChatColor.GOLD + "ILOSC" + ChatColor.GRAY + ": " + ChatColor.GRAY + amountget.get(p));
        lore.add(ChatColor.GOLD + "DATA: " + ChatColor.GRAY + d.format(new Date(System.currentTimeMillis())));
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Ten przedmiot jest wyciagniety z GameMode, ");
        lore.add(ChatColor.GRAY + "Jak najszybciej zglos to administratorowi!");
        im.setLore(lore);
        im.setDisplayName(String.valueOf(ChatColor.WHITE) + ChatColor.MAGIC + item.getType());
        item.setItemMeta(im);
        }
        Util.giveItems(p, item);
        return Util.sendMsg(sender, MainColor + "Dales " + ImportantColor + UnderLined + m.name().toLowerCase().replace("_", " ") + MainColor + " (" + ImportantColor + "x" + item.getAmount() + MainColor + ") graczowi " + ImportantColor + p.getName() + MainColor + "!");
    }
}
