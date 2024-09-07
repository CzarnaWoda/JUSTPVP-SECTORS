package pl.blackwater.guilds.commands.user;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import pl.blackwater.core.data.*;
import pl.blackwater.core.interfaces.*;
import pl.blackwater.core.managers.*;
import pl.blackwater.core.settings.*;
import pl.blackwater.guilds.data.*;
import pl.blackwater.guilds.managers.*;
import pl.blackwater.guilds.scoreboard.*;
import pl.blackwater.guilds.settings.*;
import pl.blackwater.guilds.utils.*;
import pl.blackwaterapi.commands.*;
import pl.blackwaterapi.utils.*;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildCreatePacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.*;

public class CreateCommand extends PlayerCommand implements Colors {


    public CreateCommand() {
        super("zaloz", "Tworzenie gildii", "/g zaloz <tag> <nazwa>", "guild.gracz", "create");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(args.length != 2)
            return Util.sendMsg(player,MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}",getUsage()));
        final String tag = args[0];
        final String name = args[1];

        if(GuildManager.getGuild(player) != null)
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Posiadasz juz gildie!");
        if(GuildConfig.GUILDS_CREATE_TIME > System.currentTimeMillis())
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Gildie mozna zakladac dopiero od " + Util.getDate(GuildConfig.GUILDS_CREATE_TIME) + SpecialSigns + "(" + WarningColor + Util.secondsToString((int)(GuildConfig.GUILDS_CREATE_TIME - System.currentTimeMillis()) / 1000) + SpecialSigns + ")");
        if(tag.length() < 2 || tag.length() > 4 || name.length() < 2 || name.length() > 32){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nazwa gildi musi miec od 2 do 32 znakow, tag musi miec od 2 do 4 znakow!");
        }
        if(!StringUtil.isAlphaNumeric1(tag) || !StringUtil.isAlphaNumeric(name))
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Tag oraz nazwa musza byc alfanumeryczne, oraz w tagu nie moga znalezc sie male litery!");
        if(GuildManager.getGuild(tag) != null || GuildManager.getGuild(name) != null)
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Gildia o takiej nazwie/tagu juz istnieje!");
        if(player.hasPermission("guild.items.admin")){
            final RedisPacket packet = new GuildCreatePacket(tag.toUpperCase(), name, player.getUniqueId(), player.getDisplayName());
            RedisClient.sendSectorsPacket(packet);
            Guild g = GuildManager.createLocalGuild(tag.toUpperCase(),name,player);
            final Member member = MemberManager.createMember(player, g,"OWNER");
            g.addMember(member);
            GuildManager.registerGuild(g);
            g.update(true); //INSERT DATA
            ScoreBoardNameTag.updateBoard(player);
            TitleUtil.sendTitle(player,10,100,20, ImportantColor + "Gratulacje!", MainColor + "Utworzyles " + ImportantColor + "gildie");
            return true;
        }
        final List<ItemStack> items = ItemUtil.getItems(player.hasPermission("guild.vip") ? GuildConfig.COST_CREATE_VIP : GuildConfig.COST_CREATE_NORMAL, 1);
        final int kills = (player.hasPermission("guild.vip") ? 300 : 500);
        if(ItemUtil.checkItems(items,player)){
            if(GuildConfig.COST_CREATE_LVL){
                final User u = UserManager.getUser(player);
                if(u.getKills() < kills){
                    return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wystarczajacej liczby zabojstw!");
                }
            }
            ItemUtil.removeItems(items,player);
            Guild g = GuildManager.createLocalGuild(tag.toUpperCase(),name,player);
            final Member member = MemberManager.createMember(player, g,"OWNER");
            g.addMember(member);
            GuildManager.registerGuild(g);
            g.update(true); //INSERT DATA
            final RedisPacket packet = new GuildCreatePacket(tag.toUpperCase(), name, player.getUniqueId(), player.getDisplayName());
            RedisClient.sendSectorsPacket(packet);
            ScoreBoardNameTag.updateBoard(player);
            return true;
        }else
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz wymaganych przedmiotow aby utworzyc gildie! Wpisz /g itemy aby zobaczyc liste przedmiotow");
    }
}
