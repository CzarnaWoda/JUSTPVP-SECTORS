package pl.blackwater.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.global.GlobalGiftKeysPacket;
import pl.justsectors.redis.client.RedisClient;

public class GiftCommand extends PlayerCommand implements Colors{

	public GiftCommand() {
		super("gift", "dawanie", "/gift [nazwa skrzynii] [ilosc]", "core.gift");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length < 2){
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		}
	        int value = Integer.parseInt(args[1]);
	        if (!Util.isInteger(args[1])) {
	            return Util.sendMsg(p, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Podana wartosc nie jest liczba!");
	        }
	        if (value < 0) {
	            return Util.sendMsg(p, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Podana wartosc nie jest liczba!");
	        }
	        if (args.length == 2){
	        	final GlobalGiftKeysPacket packet = new GlobalGiftKeysPacket(p.getDisplayName(),args[0],value);
				RedisClient.sendSectorsPacket(packet);
	        }
		return false;
	}

}
