package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.ItemUtil;
import pl.blackwaterapi.utils.Util;

import java.util.Arrays;
import java.util.Collections;


public class SpecialItemCommand extends PlayerCommand implements Colors {
    public SpecialItemCommand() {
        super("specialitems", "Daje graczowi specjalny item", "/specialitems [dropvoucher/servervault/moneyvoucher/rankvoucher] [addDrop/amount/money/rank] [rankTime]", "core.specialitems");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length < 2){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        if(args[0].equalsIgnoreCase("dropvoucher")){
            if(Util.isFloat(args[1])){
                final double addDrop = Double.parseDouble(args[1]);
                ItemUtil.giveItems(Collections.singletonList(Core.getSpecialItemsManager().getDropVoucher(addDrop)), player);
                Util.sendMsg(player, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Otrzymales voucher na dodatkowy " + ImportantColor + "drop")));
            }else{
                return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
            }
        }else if(args[0].equalsIgnoreCase("servervault")){
            if(Util.isInteger(args[1])){
                final int amount = Integer.parseInt(args[1]);
                ItemUtil.giveItems(Collections.singletonList(Core.getSpecialItemsManager().getServerVault(amount)),player);
                Util.sendMsg(player, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Otrzymales JustCoin " + ImportantColor + "x" + amount)));
            }
        }else if(args[0].equalsIgnoreCase("moneyvoucher")){
            if(Util.isInteger(args[1])){
                final int amount = Integer.parseInt(args[1]);
                ItemUtil.giveItems(Collections.singletonList(Core.getSpecialItemsManager().getMoneyVoucher(amount)),player);
                Util.sendMsg(player, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Otrzymales voucher na " + ImportantColor + "monety")));
            }
        }else if(args[0].equalsIgnoreCase("rankvoucher")){
            if(args.length == 3) {
                final Rank rank = Core.getRankManager().getRank(args[1]);
                final String time = args[2];
                if(rank.getName().equalsIgnoreCase("SVIP") || rank.getName().equalsIgnoreCase("VIP")) {
                    ItemUtil.giveItems(Collections.singletonList(Core.getSpecialItemsManager().getRankVoucher(rank, time)), player);
                    Util.sendMsg(player, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Otrzymales voucher na " + ImportantColor + "range " + rank.getName().toUpperCase())));
                }
            }
        }else{
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        return false;
    }
}
