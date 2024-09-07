package pl.blackwater.core.commands;

import org.bukkit.entity.Player;

import pl.blackwater.core.settings.MessageConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class HelpCommand extends PlayerCommand
{
    public HelpCommand() {
        super("pomoc", "pomoc", "/pomoc", "core.pomoc", "help");
    }
    
    public boolean onCommand(Player p, String[] args) {
    	for(String s : MessageConfig.COMMAND_HELP){
    		Util.sendMsg(p, Util.fixColor(Util.replaceString(s)));
    	}
        return false;
    }
}
