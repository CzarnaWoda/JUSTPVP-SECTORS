package pl.blackwater.guilds.commands.user;

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
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildInvitePacket;
import pl.justsectors.redis.client.RedisClient;

public class InviteCommand extends PlayerCommand implements Colors {
    public InviteCommand() {
        super("zapros", "Zaproszenie gracza do gildii", "/g zapros <gracz>", "guild.gracz", "invite");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}" , getUsage()));
        }
        Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii!");
        }
        if(!g.canInvite(player.getUniqueId())){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie jest zalozycielem ani zastepca gildii!");
        }
        if(g.getGuildMembers().size() >= g.getPlayersLimit()){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Twoja gildia osiagnela maksymalna ilosc czlonkow!");
        }
        if(g.getInvites().size() > 2){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Twoja gildia zaprosila juz 2 graczy, wpisz /g invite clear aby wyczyscic zaproszenia");
        }
        if(args[0].equalsIgnoreCase("clear")){
            g.getInvites().cleanUp();
            return Util.sendMsg(player, MainColor + "Wyczysczono zaproszenia " + ImportantColor + " twojej gildii");
        }else{
            final User u = UserManager.getUser(args[0]);
            if(u == null){
                return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
            }
            if(g.isMember(u.getUuid())){
                return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Gracz jest juz czlonkiem gildii!");
            }
            final RedisPacket packet = new GuildInvitePacket(g.getTag(), player.getDisplayName(), u.getLastName(), u.getUuid());
            RedisClient.sendSectorsPacket(packet);
            return true;
        }
    }
}
