package pl.blackwater.guilds.scoreboard;


import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwater.guilds.managers.WarManager;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwaterapi.utils.Util;

import java.util.List;
import java.util.UUID;

public class ScoreBoardNameTag {
    private static Scoreboard scoreboard = new Scoreboard();
    public static boolean scoreboards = true;

    public static void initPlayer(Player p) {
        try {
            final ScoreboardTeam hasTeam = scoreboard.getTeam(p.getName());
            if(hasTeam != null && scoreboard.getPlayerTeam(p.getName()) == null){
                scoreboard.removeTeam(hasTeam);
            }
            ScoreboardTeam team = scoreboard.getPlayerTeam(p.getName());
            if(team == null) {
                team = scoreboard.createTeam(p.getName());
            }
            //ScoreboardTeam team = scoreboard.getPlayerTeam(p.getName()) == null ? scoreboard.createTeam(p.getName()) : scoreboard.getPlayerTeam(p.getName());
            scoreboard.addPlayerToTeam(p.getName(), team.getName());
            team.setPrefix("");
            team.setDisplayName("");
            team.setSuffix("");
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 0);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp != p) {
                    ((CraftPlayer) pp).getHandle().playerConnection.sendPacket(packet);
                }
            }
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp != p) {
                    ScoreboardTeam t = scoreboard.getTeam(pp.getName());
                    PacketPlayOutScoreboardTeam packetShow = new PacketPlayOutScoreboardTeam((t != null ? t : scoreboard.createTeam(pp.getName())), 0);
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetShow);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateGuildTag(final Guild guild)
    {
        if(guild == null) return;
        for(Member member : guild.getOnlineMembers())
        {
            final Player p = Bukkit.getPlayer(member.getUuid());
            if(p != null) {
                updateBoard(p);
            }
        }
    }


    public static void updateBoard(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getPlugin(), () -> {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        updateOthersFor(p, online);
                        updateOthersFor(online, p);
                    }
                }
        );
    }

    private static void updateOthersFor(Player send, Player p) {
        ScoreboardTeam team = scoreboard.getPlayerTeam(p.getName());
        User get = UserManager.getUser(p.getUniqueId());
        User s = UserManager.getUser(send.getUniqueId());
        team.setPrefix(getValidPrefix(get, s));
        team.setSuffix(getValidSuffix(get));
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 2);
        ((CraftPlayer) send).getHandle().playerConnection.sendPacket(packet);
    }


    private static String getValidPrefix(User get, User send) {
        ChatColor color = ChatColor.GRAY;
        ChatColor color2 = ChatColor.WHITE;
        Guild getg = get.getGuild();
        Guild sendg = send.getGuild();
        if (getg != null && sendg != null) {
            if (getg.getTag().equals(sendg.getTag())) {
                color = ChatColor.BLUE;
            } else if (WarManager.hasWar(getg,sendg)) {
                color = ChatColor.DARK_RED;
            }
        }
        String tag = "";
        if (getg != null) {
            tag = ChatColor.DARK_GRAY + "[" + color + getg.getTag() + ChatColor.DARK_GRAY + "] ";
            color2 = color;
        }
        return tag + color2;
    }
    private static String getValidSuffix(User get){
        if(get.getRank().equalsIgnoreCase("headadmin")){
            return  Util.fixColor(Util.replaceString(GuildConfig.TAG_HEADADMIN_SUFFIX));
        }
        if(get.getRank().equalsIgnoreCase("admin")){
            return  Util.fixColor(Util.replaceString(GuildConfig.TAG_ADMIN_SUFFIX));
        }
        if(get.getRank().equalsIgnoreCase("moderator")){
            return  Util.fixColor(Util.replaceString(GuildConfig.TAG_MODERATOR_SUFFIX));
        }
        if(get.getRank().equalsIgnoreCase("helper")){
            return Util.fixColor(Util.replaceString(GuildConfig.TAG_HELPER_SUFFIX));
        }
        if(get.getRank().equalsIgnoreCase("svip")){
            return Util.fixColor(Util.replaceString(GuildConfig.TAG_SVIP_SUFFIX));
        }
        if(get.getRank().equalsIgnoreCase("vip")){
            return Util.fixColor(Util.replaceString(GuildConfig.TAG_VIP_SUFFIX));
        }
        return "";
    }



    public static void removeBoard(Player p) {
        try {
            ScoreboardTeam team = null;
            if (scoreboard.getPlayerTeam(Bukkit.getPlayer(p.getName()).getName()) == null) {
                return;
            }
            team = scoreboard.getPlayerTeam(p.getName());
            scoreboard.removePlayerFromTeam(p.getName(), team);
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 1);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp != p) {
                    ((CraftPlayer) pp).getHandle().playerConnection.sendPacket(packet);
                }
            }
            for (Player pp : Bukkit.getServer().getOnlinePlayers()) {
                if (pp != p) {
                    ScoreboardTeam t = scoreboard.getTeam(pp.getName());
                    PacketPlayOutScoreboardTeam packetHide = new PacketPlayOutScoreboardTeam(t, 1);
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetHide);
                }
            }
            scoreboard.removeTeam(team);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateBoard(Guild guild) {
        for (Member member : guild.getOnlineMembers()) {
            final Player pl = Bukkit.getPlayer(member.getUuid());
            if (pl != null && pl.isOnline()) {
                User user = UserManager.getUser(pl);
                Player p = user.getPlayer();
                if (p != null) {
                    ScoreBoardNameTag.updateBoard(p);
                }
            }
        }
    }
}
