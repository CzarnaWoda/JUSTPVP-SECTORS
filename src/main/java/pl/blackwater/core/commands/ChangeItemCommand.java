package pl.blackwater.core.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

import java.util.Collections;

public class ChangeItemCommand extends PlayerCommand implements Colors
{

	public ChangeItemCommand()
	{
		super("changeitem", "zmienia nazwe lub lore przedmiotu w rece", "/changeitem [nazwa/lore] <napis>", "core.changeitem", "itemrename");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length < 2) return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		if(p.getItemInHand() == null) return Util.sendMsg(p, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + " W twojej rece nie ma zadnego przedmiotu!");
		ItemStack item = p.getItemInHand();
		ItemMeta meta = item.getItemMeta();
		switch (args[0]) {
		case "nazwa":
			meta.setDisplayName(Util.fixColor(Util.replaceString(StringUtils.join(args," ",1,args.length))));
			item.setItemMeta(meta);
			p.updateInventory();
			break;
		case "lore":
			meta.setLore(Collections.singletonList(Util.fixColor(Util.replaceString(StringUtils.join(args, " ", 1, args.length)))));
			item.setItemMeta(meta);
			p.updateInventory();
			break;
		default:
			Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
			break;
		}
		return false;
	}

}
