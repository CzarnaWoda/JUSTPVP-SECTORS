package pl.blackwater.core.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.data.APIConfig;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.chat.ChatMessagePacket;
import pl.justsectors.packets.impl.user.UserKickPacket;
import pl.justsectors.redis.client.RedisClient;

public class KickCommand extends Command implements Colors
{

	public KickCommand()
	{
		super("kick", "wyrzucanie gracza z serwera", "/kick <gracz> <powod>", "core.kick", "wyrzuc");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] args) {
		if(args.length < 1) return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}", getUsage()));
		final User user = UserManager.getUser(args[0]);
		if(user == null) return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
		if(!user.isOnline()) return Util.sendMsg(sender, MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
		if(APIConfig.SUPERADMINSYSTEM_ADMINUUID.contains(user.getUuid().toString())) return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Nie mozesz wyrzucic tego gracza!");
		if(user.getLastName().equals(sender.getName())) return Util.sendMsg(sender, WarningColor + "Blad" + SpecialSigns + ": " + WarningColor_2 + "Nie mozesz wyrzucic samego siebie!");
		String reason = "Administrator ma zawsze racje!";
		if(args.length > 1) reason = StringUtils.join(args, " ",1,args.length);
		final String kickMsg =Util.fixColor(Util.replaceString(MessageConfig.SERVERNAME_TAG + "\n \n" + WarningColor + "             Zostales wyrzucony z serwera\n" + SpecialSigns + "» " + MainColor + "Przez: " + ImportantColor + sender.getName() + "\n" + SpecialSigns + "» " + MainColor + "Powod: " + ImportantColor + UnderLined + reason + "\n\n" + SpecialSigns + "» " + MainColor + "Kontakt TeamSpeak3:" + ImportantColor + "justpvp.pl"));
		final UserKickPacket userKickPacket = new UserKickPacket(user.getUuid(),reason);
		RedisClient.sendPacket(userKickPacket,user.getSector());
		final ChatMessagePacket chatMessagePacket = new ChatMessagePacket(Util.fixColor(Util.replaceString(SpecialSigns + ">> " + MainColor + "Gracz " + ImportantColor + user.getLastName() + MainColor + " zostal wyrzucony z serwera przez " + ImportantColor + sender.getName() + SpecialSigns + " (" + ImportantColor + UnderLined + reason + SpecialSigns + ")")));
		RedisClient.sendSectorsPacket(chatMessagePacket);
		return false;
	}
	

}
