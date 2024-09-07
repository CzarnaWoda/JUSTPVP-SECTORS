package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.core.tasks.ChickenTask;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class FunCommand extends PlayerCommand {
    public FunCommand() {
        super("fun", "FunCommand xD", "/fun wagonik", "core.admin.fun", "jebacbao");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length < 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        if(args[0].equalsIgnoreCase("wagonik")){
            new ChickenTask(player).runTaskTimer(Core.getPlugin(),1L,1L);
            Util.sendMsg(player,"&4&lBAO MA ZAWSZE RACJE");
        }
        return false;
    }
}
