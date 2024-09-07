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
import pl.justsectors.packets.impl.guild.GuildExpireExtendPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.*;

public class ExpireCommand extends PlayerCommand implements Colors {
    public ExpireCommand() {
        super("przedluz", "Przedluzenie waznosci gildii", "/g przedluz", "guild.gracz", "expire", "oplac");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii");
        }
        if(!g.canExpire(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie jestes wlascicielem,zastepca ani moderatorem gildii");
        }
        final long time = g.getExpireTime() - System.currentTimeMillis();
        if(time > 1209600000L){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Gildia juz jest wazna na ponad 14 dni!");
        }
        final List<ItemStack> items = ItemUtil.getItems(GuildConfig.COST_EXPIRE_NORMAL,1);
        if(ItemUtil.checkAndRemove(items,player)){
            g.addOneWeekToExpireTime();
            g.update(true);
            final RedisPacket packet = new GuildExpireExtendPacket(g.getTag(), player.getDisplayName(), g.getExpireTime());
            RedisClient.sendSectorsPacket(packet);
        }else{
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wymaganych przedmiotow!" + "\n" + WarningColor + "Blad: " + WarningColor_2 + "Potrzebujesz: " + ItemUtil.getItems(items));
        }
        return false;
    }
}
