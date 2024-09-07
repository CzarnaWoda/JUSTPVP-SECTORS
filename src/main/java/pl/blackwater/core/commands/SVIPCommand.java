package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class SVIPCommand extends PlayerCommand implements Colors{

	public SVIPCommand() {
		super("svip", "informacje o randze premium", "/svip", "core.svip.command");
	}

	@Override
	public boolean onCommand(Player sender, String[] arg1) {
		for(String s : MessageConfig.COMMAND_SVIP)
			if(!s.equalsIgnoreCase("{ITEMSHOP}"))
				Util.sendMsg(sender, Util.fixColor(Util.replaceString(s.replace("{PLAYER}", sender.getName()))));
			else
				Util.SendOpen_URLTextComponent(sender, Util.fixColor(Util.replaceString(SpecialSigns + "    * " + MainColor + "Przejdz do " + ImportantColor + "ItemShop'u")), "http://www.sklep.justpvp.pl/", MainColor + "Kliknij aby przejsc do " + ImportantColor + "ItemShop'u");
		return false;
	}

}
