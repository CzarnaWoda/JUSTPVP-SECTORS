package pl.blackwater.guilds.commands.user;

import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.settings.MessageConfig;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.War;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.WarManager;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwater.guilds.utils.DateUtil;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildWarStartPacket;
import pl.justsectors.redis.client.RedisClient;

public class WarCommand extends PlayerCommand implements Colors {
    public WarCommand() {
        super("wojna", "Wypowiada przeciwnej gildi wojne", "/g wojna <gildia>", "guild.gracz", "war");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(!GuildConfig.ENABLEMANAGER_WAR)
            return Util.sendMsg(player,WarningColor + "Blad: "  + WarningColor_2 + "Wojny zostaly globalnie wylaczone przez administratora");
        if(args.length < 1){
            return Util.sendMsg(player, MessageConfig.MESSAGE_COMMAND_GETUSAGE.replace("{USAGE}" , getUsage()));
        }
        Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii!");
        }
        if(!g.isOwner(player.getUniqueId())){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie jestes zalozycielem gildii!");
        }
        Guild o = GuildManager.getGuild(args[0]);
        if(o == null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Taka gildia nie istnieje!");
        }
        if(g.equals(o)){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Nie mozesz wywolac tej gildii wojny!");
        }
        if(WarManager.hasWar(g,o)){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Masz juz wojne z ta gildia");
        }
        if(o.getCreateTime() + 1800000L >= System.currentTimeMillis()){
            return Util.sendMsg(player,"&4Blad: &cGildia zostala dopiero zalozona!");
        }
        if(WarManager.getWar(g) != null || WarManager.getWar(o) != null){
            return Util.sendMsg(player, WarningColor + "Blad: " + WarningColor_2 + "Gildia moze maksymalnie uczestniczyc w jednej wojnie");
        }
        final String from = "12:00";
        final String to = "21:00";
        if(!DateUtil.isNowInInterval(from,to)){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Wojne mozna wypowiadac pomiedzy godzina 12:00 a 21:00");
        }
        War war = WarManager.createWar(g,o,Util.parseDateDiff(GuildConfig.WARMANAGER_WARTIME,true));
        if(war != null)
        {
            war.insert();
        }
        final RedisPacket packet = new GuildWarStartPacket(g.getTag(), g.getName(), o.getTag(), o.getName());
        RedisClient.sendSectorsPacket(packet);
        return true;
    }
}
