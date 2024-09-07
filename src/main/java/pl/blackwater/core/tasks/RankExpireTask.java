package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.core.data.RankSet;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.RankSetManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.utils.ActionBarUtil;
import pl.blackwaterapi.utils.Util;

public class RankExpireTask extends BukkitRunnable implements Colors {
    @Override
    public void run() {
        for(Player online : Bukkit.getOnlinePlayers()){
            final RankSet rankSet = RankSetManager.getSetRank(UserManager.getUser(online));
            if(rankSet != null){
                if(rankSet.getExpireTime() != 0L && rankSet.getExpireTime() <= System.currentTimeMillis()){
                    ActionBarUtil.sendActionBar(online, Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Twoja ranga " + ImportantColor + rankSet.getRank() + MainColor + " wlasnie " + ImportantColor + " wygasla")));
                    RankSetManager.removeRank(UserManager.getUser(online), rankSet);
                }
            }
        }
    }
}
