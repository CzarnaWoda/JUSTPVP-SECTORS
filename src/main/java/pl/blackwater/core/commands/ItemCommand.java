package pl.blackwater.core.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.core.utils.MaterialUtil;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

import java.util.concurrent.atomic.AtomicReference;

public class ItemCommand extends PlayerCommand implements Colors{

	public ItemCommand() {
		super("item", "daje przedmiot graczowi", "/item <id> [ilosc]", "core.item", "i");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if (args.length == 1 || args.length == 2) {
            String[] idAndData = args[0].split(":");
            Material material = MaterialUtil.getMaterial(idAndData[0]);
            if (material == null) {
            	return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie rozpoznano nazwy przedmiotu!");
            }
            short data = 0;
            if (idAndData.length > 1) {
                data = Short.parseShort(idAndData[1]);
            }
            int amount = 64;
            if (args.length == 2 && Util.isInteger(args[1])) {
                amount = Integer.parseInt(args[1]);
            }
            AtomicReference<ItemStack> item = new AtomicReference<>(new ItemStack(material, amount, data));
            if (p.getInventory().firstEmpty() < 0) {
            	return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz miejsca w ekwipunku!");
            }
            p.getInventory().addItem(item.get());
            return Util.sendMsg(p, MainColor + "Otrzymales " + ImportantColor + material.name() + MainColor + ":" + ImportantColor + data + SpecialSigns + "(" + ImportantColor + amount + SpecialSigns + ")");
        }
        else {
            return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
        }
    }
	
}
