package pl.blackwater.guilds.commands.user;

import org.bukkit.*;
import org.bukkit.entity.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildLeavePacket;
import pl.justsectors.redis.client.RedisClient;

public class LeaveCommand extends PlayerCommand implements Colors {
    public LeaveCommand() {
        super("opusc", "Opuszczanie gildii", "/g opusc", "guild.gracz", "leave");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        final Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii");
        }
        if(g.isOwner(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Zalozyciel gildii nie moze jej opuscic");
        }
        final War w = WarManager.getWar(g);
        if(w != null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz zostawic swoich braci na polu bitwy");
        }
        if(g.isMember(player.getUniqueId())) {
            g.removeMember(player.getUniqueId());
            g.update(true);
            final RedisPacket packet = new GuildLeavePacket(g.getTag(), player.getUniqueId(), player.getDisplayName());
            RedisClient.sendSectorsPacket(packet);
        }
        return true;
    }
}
