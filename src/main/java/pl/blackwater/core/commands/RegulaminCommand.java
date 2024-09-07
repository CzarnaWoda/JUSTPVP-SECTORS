package pl.blackwater.core.commands;

import org.bukkit.command.CommandSender;

import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.Command;
import pl.blackwaterapi.utils.Util;

public class RegulaminCommand extends Command
{

	public RegulaminCommand()
	{
		super("regulamin", "pokazuje regulamin serwera", "/regulamin", "core.regulamin", "zasady");
	}

	@Override
	public boolean onExecute(CommandSender sender, String[] arg1) {
		for(String s : MessageConfig.COMMAND_REGULAMIN){
			Util.sendMsg(sender, Util.replaceString(s));
		}
		return false;
	}

}
