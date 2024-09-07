package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class SpeedCommand extends PlayerCommand implements Colors{

	public SpeedCommand() {
		super("speed", "ustawienie predkosci latania/chodzenia", "/speed [wartosc]", "core.speed");
	}

	@Override
	public boolean onCommand(Player p, String[] args) {
		if(args.length != 1)
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		if(!Util.isInteger(args[0]))
			return Util.sendMsg(p, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		final float speed = Float.parseFloat(args[0]);
		if(speed > 10.0f || speed < 1.0f)
			return Util.sendMsg(p, WarningColor + "Blad: " + WarningColor_2 + "Maksymalna predkosc chodzenia/latania to 10 a minimalna to 1");
		final float finalSpeed = speed/10.0f;
		if(p.isFlying()) {
			p.setFlySpeed(finalSpeed);
			return Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ustawiles predkosc " + ImportantColor + "latania " + MainColor + " na " + ImportantColor + speed)));
		}
		else {
			p.setWalkSpeed(finalSpeed);
			return Util.sendMsg(p, Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Ustawiles predkosc " + ImportantColor + "chodzenia " + MainColor + " na " + ImportantColor + speed)));
		}
	}
	
	

}
