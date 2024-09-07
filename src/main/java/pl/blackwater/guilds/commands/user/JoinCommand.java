package pl.blackwater.guilds.commands.user;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.settings.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwater.guilds.scoreboard.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildJoinPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.*;

public class JoinCommand extends PlayerCommand implements Colors {
    public JoinCommand() {
        super("dolacz", "Przyjmujmowanie zaproszenia do gildii", "/g dolacz <gildia>", "guild.gracz", "join");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length != 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        }
        if(GuildManager.getGuild(player) != null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Posiadasz juz gildie");
        }
        final Guild g = GuildManager.getGuild(args[0]);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Taka gildia nie istnieje");
        }
        if(!g.hasInvite(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz zaproszenia to tej gildii");
        }
        if(g.getGuildMembers().size() >= g.getPlayersLimit()){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Ta gildia ma juz maksymalna liczbe czlonkow! &8(&4" + g.getPlayersLimit() + "&8)");
        }
        if(WarManager.getWar(g) != null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Gildia aktualnie jest podczas wojny");
        }
        final List<ItemStack> items = new ArrayList<>();
        int calculated = (int)((g.getGuildMembers().size() - 1) * 3.5 + 39.0 + 3.5);
        calculated /= 2;
        items.add(new ItemStack(Material.DIAMOND,calculated));
        if(!ItemUtil.checkAndRemove(items,player)){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Aby dolaczyc do gildi potrzebujesz " + calculated + " diamentow");
        }else{
            final Member member = MemberManager.createMember(player,g,"NORMAL");
            g.addMember(member);
            g.insert();
            final RedisPacket packet = new GuildJoinPacket(g.getTag(), player.getDisplayName(), member.getUuid());
            RedisClient.sendSectorsPacket(packet);
        }
        return false;
    }
}
