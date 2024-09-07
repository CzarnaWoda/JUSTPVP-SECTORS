package pl.blackwater.guilds.commands.user;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.*;
import org.bukkit.entity.*;
import pl.blackwater.core.data.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.managers.*;
import pl.blackwater.core.settings.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwater.guilds.scoreboard.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildKickPacket;
import pl.justsectors.redis.client.RedisClient;

public class KickCommand extends PlayerCommand implements Colors {
    public KickCommand() {
        super("wyrzuc", "Wyrzucanie gracza z gildii", "/g wyrzuc <gracz>", "guild.gracz", "kick");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if (args.length != 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        final Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii");
        }
        if(!g.canKick(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie jestes uprawniony do wyrzucania gracza z gildii");
        }
        final User user = UserManager.getUser(args[0]);
        if(user == null){
            return Util.sendMsg(player,MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
        }
        if(!g.isMember(user.getUuid())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Gracz nie jest czlonkiem gildii");
        }
        g.removeMember(user.getUuid());
        g.update(true);
        final RedisPacket packet = new GuildKickPacket(g.getTag(), user.getUuid(), user.getLastName(), player.getDisplayName());
        RedisClient.sendSectorsPacket(packet);
        return false;
    }
}
