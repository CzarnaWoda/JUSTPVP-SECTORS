package pl.blackwater.core.tasks;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Mine;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.MineManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.Member;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwater.guilds.ranking.RankingManager;
import pl.blackwaterapi.utils.Util;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResetBreakStoneDayTask extends BukkitRunnable{
    private static boolean clear = false;
    private static boolean change = false;
    private final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    @Override
    public void run() {
        Calendar c = Calendar.getInstance();
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);
        int d = c.get(Calendar.DAY_OF_WEEK);
        if(h == 23 && m == 59 && !clear){
            for(User u : UserManager.getUsers().values()){
                executorService.submit(() -> {
                    u.setBreakStoneDay(0);
                    u.update(true);
                });
            }
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.hasPermission("core.admin")){
                    Util.sendMsg(p, "&4Zmieniono wartosc breakstoneday na 0 dla wszystkich zarejestrowanych uzytkownikow justpvp.pl");
                }
            }
            clear = true;
        }
        if(h == 1 && m == 0 && clear){
            clear = false;
        }
        if(h == 23 && m == 59 && d == 5 && !change){
            Guild g = RankingManager.getGuildRankings().get(0);
            if(GuildManager.getGuilds().size() >= 1){
                for(Mine mine : MineManager.getMines().values()){
                    if(mine.getType().equalsIgnoreCase("GUILD")){
                        if(!mine.getGuild().equalsIgnoreCase(g.getTag())){
                            if(!mine.getGuild().equals("")){
                                ProtectedRegion mainRegion = mine.getProtectedMainRegion();
                                ProtectedRegion stoneRegion = mine.getProtectedStoneRegion();
                                mainRegion.getMembers().removeAll();
                                stoneRegion.getMembers().removeAll();
                            }
                            ProtectedRegion mainRegion = mine.getProtectedMainRegion();
                            ProtectedRegion stoneRegion = mine.getProtectedStoneRegion();
                            for(Member member : g.getGuildMembers().values()){
                                LocalPlayer localplayer = Objects.requireNonNull(Core.getWorldGuard()).wrapOfflinePlayer(Bukkit.getOfflinePlayer(member.getUuid()));
                                mainRegion.getMembers().addPlayer(localplayer);
                                stoneRegion.getMembers().addPlayer(localplayer);
                            }
                            mine.setGuild(g.getTag());
                        }
                    }
                }
                change = true;
            }
        }
        if(h == 12 && m == 0 && change){
            change = false;
        }

    }

}