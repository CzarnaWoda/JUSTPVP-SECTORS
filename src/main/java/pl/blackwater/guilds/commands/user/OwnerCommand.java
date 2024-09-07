package pl.blackwater.guilds.commands.user;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import pl.blackwater.core.data.User;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwater.guilds.settings.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildSetOwnerPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.*;

public class OwnerCommand extends PlayerCommand implements Colors {
    public OwnerCommand() {
        super("zalozyciel", "Nadaje zalozyciela gildii", "/g zalozyciel <gracz>", "guild.gracz", "owner");
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
        final User o = UserManager.getUser(args[0]);
        if(o == null)
            return Util.sendMsg(player,MessageConfig.MESSAGE_COMMAND_UNKNOWNUSER);
        if(!g.isMember(o.getUuid())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Gracz nie jest czlonkiem gildii");
        }
        final List<ItemStack> itemStackList = ItemUtil.getItems(GuildConfig.COST_LEADER_NORMAL,1);
        if(!ItemUtil.checkAndRemove(itemStackList,player)){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wymaganych przedmiotow! " + SpecialSigns + "(" + WarningColor + ItemUtil.getItems(itemStackList) + SpecialSigns + ")");
        }else{
            g.setOwner(o.getUuid());
            g.update(true);
            final RedisPacket packet = new GuildSetOwnerPacket(g.getTag(), o.getUuid(), o.getLastName(), player.getDisplayName());
            RedisClient.sendSectorsPacket(packet);
        }
        return false;
    }
}
