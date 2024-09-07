package pl.blackwater.core.commands;

import org.bukkit.command.CommandSender;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.configs.ConfigManager;
import pl.blackwaterapi.utils.Util;

public class SlotCommand extends Command implements Colors
{
    public SlotCommand() {
        super("slot", "ustawianie liczby slotow", "/slot <liczba>", "core.slot");
    }

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if (args.length == 0)
			return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		if(!Util.isInteger(args[0]))
			return Util.sendMsg(sender, WarningColor + "Blad: " + WarningColor_2 + "Podana wartosc nie jest liczba!");
		int slots = CoreConfig.SLOTMANAGER_SLOTS = Integer.parseInt(args[0]);
		ConfigCreator config = ConfigManager.getConfig("config.yml");
		config.setField("slotmanager.slots", slots);
		return Util.sendMsg(sender, Util.replaceString(SpecialSigns + ">> " + MainColor + "Ustawiono ilosc slotow na " + ImportantColor + slots));
	}

}
