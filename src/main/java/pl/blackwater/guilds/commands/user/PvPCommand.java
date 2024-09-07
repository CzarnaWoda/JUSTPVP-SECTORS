package pl.blackwater.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.guilds.data.Alliance;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.managers.AllianceManager;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwaterapi.commands.PlayerCommand;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.impl.guild.GuildSetPvPPacket;
import pl.justsectors.redis.client.RedisClient;

import java.util.Arrays;
import java.util.List;

public class PvPCommand extends PlayerCommand implements Colors {

    public PvPCommand() {
        super("pvp", "Zmienia status pvp w gildii", "/g pvp", "guild.gracz", "ff", "friendlyfire");
    }

    @Override
    public boolean onCommand(Player player, String[] args) {
        final Guild g = GuildManager.getGuild(player);
        if(g == null){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii");
        }
        if(!g.isCanChangePvP(player.getUniqueId())){
            return Util.sendMsg(player,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz uprawnien aby wlaczyc/wylaczyc pvp");
        }
        final RedisPacket packet = new GuildSetPvPPacket(g.getTag(), player.getDisplayName());
        RedisClient.sendSectorsPacket(packet);
        return true;
    }
}
