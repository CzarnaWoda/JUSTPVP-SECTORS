package pl.blackwater.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.War;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwater.guilds.managers.WarManager;
import pl.blackwater.guilds.scoreboard.ScoreBoardNameTag;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildRemovePacket;
import pl.justsectors.redis.client.RedisClient;

public class DeleteCommand extends PlayerCommand implements Colors {
    public DeleteCommand() {
        super("usun", "Usuwa gildie", "/g usun", "guild.gracz", "delete");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        final Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii");
        }
        if(!g.isOwner(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie jestes zalozycielem gildii");
        }
        if(!g.isPreDeleted()){
            g.setPreDeleted(true);
            Util.sendMsg(player,MainColor + "Aby potwierdzic usuniecie gildii wpisz ponownie &2/g usun");
            Bukkit.getScheduler().runTaskLater(Core.getPlugin(), () -> g.setPreDeleted(false),200L);
            return true;
        }
        g.delete();
        final RedisPacket packet = new GuildRemovePacket(g.getTag(), player.getDisplayName());
        RedisClient.sendSectorsPacket(packet);
        return true;
    }
}
