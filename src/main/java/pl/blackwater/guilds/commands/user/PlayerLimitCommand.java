package pl.blackwater.guilds.commands.user;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwater.guilds.settings.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildPlayerLimitPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.*;

public class PlayerLimitCommand extends PlayerCommand implements Colors {

    public PlayerLimitCommand() {
        super("limit", "Powiekszanie limitu czlonkow", "/g limit", "guild.gracz", "max");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        final Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii");
        }
        if(!g.isCanPlayerLimit(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien aby powiekszyc limit graczy w gildii");
        }
        final List<ItemStack> itemStackList = ItemUtil.getItems(GuildConfig.COST_LIMIT_NORMAL, 1);
        if(!ItemUtil.checkItems(itemStackList, player)){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wymaganych przedmiotow!" + "\n" + WarningColor + "Blad: " + WarningColor_2 + "Potrzebujesz: " + ItemUtil.getItems(itemStackList));
        }else {
            if (g.getPlayersLimit() < 20) {
                ItemUtil.removeItems(itemStackList, player);
                final int toSet = g.getPlayersLimit() + 1;
                g.setPlayersLimit(toSet);
                g.update(true);
                final RedisPacket packet = new GuildPlayerLimitPacket(g.getTag(), player.getDisplayName(), toSet);
                RedisClient.sendSectorsPacket(packet);
            }else{
                return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Twoja gildia posiada maksymalna ilosc czlonkow &8(&420&8)");
            }
        }
        return false;
    }
}
