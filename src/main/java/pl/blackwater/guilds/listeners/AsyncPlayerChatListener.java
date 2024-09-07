package pl.blackwater.guilds.listeners;

import io.netty.buffer.ByteBuf;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.blackwater.core.Core;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.guilds.data.Alliance;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.managers.AllianceManager;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwaterapi.utils.Util;
import pl.blazingpack.waypoints.WaypointBuilder;

import java.util.concurrent.TimeUnit;

public class AsyncPlayerChatListener implements Listener, Colors {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
        localChat(e);
    }

    private void localChat(AsyncPlayerChatEvent e){
        final Player p = e.getPlayer();
        final String msg = e.getMessage();
        final Guild g = GuildManager.getGuild(p);
        if(msg.startsWith("!!")){
            if(g == null){
                Util.sendMsg(p,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii!");
                e.setCancelled(true);
                return;
            }
            final String format = "&6" + g.getTag() + "    " + p.getName() + " ->> SOJUSZ: " + ChatColor.stripColor(Util.fixColor(msg))
                    .replaceFirst("!!","").replace("help","POMOCY - X:" + p.getLocation().getBlockX() + " Z:" + p.getLocation().getBlockZ());
            for (Alliance a : AllianceManager.getGuildAlliances(g)) {
                Guild o = a.getGuild1().equals(g) ? a.getGuild2() : a.getGuild1();
                for (Member onlineMember : o.getOnlineMembers()) {
                    final Player pl = Bukkit.getPlayer(onlineMember.getUuid());
                    if(pl != null && pl.isOnline()){
                        Util.sendMsg(pl, format);
                    }
                }
            }
            for (Member onlineMember : g.getOnlineMembers()) {
                final Player pl = Bukkit.getPlayer(onlineMember.getUuid());
                if(pl != null && pl.isOnline()){
                    Util.sendMsg(pl, format);
                }
            }
            e.setCancelled(true);
        }else if (msg.startsWith("!")){
            if(g == null){
                Util.sendMsg(p,WarningColor + "Blad: " + WarningColor_2 + "Nie posiadasz gildii!");
                e.setCancelled(true);
                return;
            }
            final String format = "&2" + g.getTag() + "    " + p.getName() + " ->> GILDIA: " + ChatColor.stripColor(Util.fixColor(msg))
                    .replaceFirst("!!","").replace("help","POMOCY - X:" + p.getLocation().getBlockX() + " Z:" + p.getLocation().getBlockZ());
            for(Member m : g.getOnlineMembers()){
                final  Player mp = Bukkit.getPlayer(m.getUuid());
                Util.sendMsg(mp,format);
                e.setCancelled(true);
            }
        }
    }
}
