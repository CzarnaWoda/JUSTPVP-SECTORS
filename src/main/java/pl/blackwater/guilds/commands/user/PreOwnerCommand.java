package pl.blackwater.guilds.commands.user;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.settings.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwater.guilds.settings.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildSetPreOwnerPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.*;

public class PreOwnerCommand extends PlayerCommand implements Colors {
    public PreOwnerCommand() {
        super("zastepca", "Nadaje zastepce gildii", "/g zastepca <gracz>", "guild.gracz");
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
        if(!g.isOwner(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie jestes zalozycielem gildii");
        }
        final Player o = Bukkit.getPlayer(args[0]);
        if(o == null)
            return Util.sendMsg(player,MessageConfig.MESSAGE_COMMAND_UNKNOWNPLAYER);
        if(!g.isMember(o.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Gracz nie jest czlonkiem gildii");
        }
        final List<ItemStack> itemStackList = ItemUtil.getItems(GuildConfig.COST_LEADER_NORMAL,1);
        if(!ItemUtil.checkAndRemove(itemStackList,player)){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wymaganych przedmiotow! " + SpecialSigns + "(" + WarningColor + ItemUtil.getItems(itemStackList) + SpecialSigns + ")");
        }
        g.setPreOwner(o.getUniqueId());
        g.update(true);
        final RedisPacket packet = new GuildSetPreOwnerPacket(g.getTag(), o.getDisplayName(), o.getUniqueId(), player.getDisplayName());
        RedisClient.sendSectorsPacket(packet);
        return true;
    }
}
