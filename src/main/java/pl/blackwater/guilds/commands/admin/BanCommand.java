package pl.blackwater.guilds.commands.admin;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;

public class BanCommand extends PlayerCommand implements Colors {

    public BanCommand() {
        super("ban", "banowanie gildii", "/ga ban <gildia> <czas> <powod>", "guild.admin");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length < 3) {
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        final Guild g = GuildManager.getGuild(args[0]);
        if (g == null) {
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Taka gildia nie istnieje");
        }
        final long time = Util.parseDateDiff(args[1], true);
        final String reason = StringUtils.join(args, " ", 2, args.length);
        //for (Member member : MemberManager.getGuildMembers(g)) {
            //TODO BanManager.createBan(member.getUuid(), reason + " (GILDIA)", player.getName(), time);
     //   }
        Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("&8->> &4Gildia &8[&c" + g.getTag() + "&8]&c" + g.getName() + "&4zostala zbanowana przez " + player.getName() + " do " + Util.getDate(time))));
        return false;
    }
}
