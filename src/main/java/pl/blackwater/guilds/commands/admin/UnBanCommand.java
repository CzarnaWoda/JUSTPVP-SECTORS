package pl.blackwater.guilds.commands.admin;

import org.bukkit.*;
import org.bukkit.entity.*;
import pl.blackwater.core.data.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.managers.*;
import pl.blackwater.core.settings.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;

public class UnBanCommand extends PlayerCommand implements Colors {
    public UnBanCommand() {
        super("unban", "odbanowywanie gildii", "/ga unban <gildia>", "guild.admin");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        final Guild g = GuildManager.getGuild(args[0]);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Taka gildia nie istnieje");
        }
        int c = 0;
        //for(Member m : MemberManager.getGuildMembers(g)){
            //Ban b = BanManager.getBan(m.getUuid());
            //if(b != null && b.getReason().contains("(GILDIA)")){
               // b.setUnban(true);
               // BanManager.deleteBan(b);
               // c++;
            //TODO}
        //}
        if(c > 0){
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("&8->> &4Gildia &8[&c" + g.getTag() + "&8]&c" + g.getName() + "&4zostala odbanowana przez " + player.getName())));
        }
        return true;
    }
}
