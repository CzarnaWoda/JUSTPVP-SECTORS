package pl.blackwater.guilds.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.guilds.data.Guild;
import pl.blackwater.guilds.data.War;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwater.guilds.managers.WarManager;
import pl.blackwaterapi.utils.TitleUtil;
import pl.blackwaterapi.utils.Util;

public class CheckValidityTask extends BukkitRunnable implements Colors {


    public void run() {
        for (Guild g : GuildManager.getGuilds().values()) {
            if (g.getExpireTime() < System.currentTimeMillis()) {
                final War war = WarManager.getWar(g);
                if(war != null) {
                    Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "|->> " + WarningColor + "WOJNA " + MainColor + "pomiedzy gildia " + ImportantColor + war.getGuild1().getTag() + MainColor + " a " + ImportantColor + war.getGuild2().getTag() + MainColor + " wlasnie sie " + ImportantColor + "zakonczyla" + MainColor + ", wynik wojny: " + ImportantColor + war.getGuild1().getTag() + SpecialSigns + " >> " + ImportantColor + war.getKills1() + MainColor + " %X% " + ImportantColor + war.getGuild2().getTag() + SpecialSigns + " >> " + ImportantColor + war.getKills2())));
                    if (war.getGuild1().equals(g)) {
                        war.getGuild2().addWinWars(1);
                        war.getGuild1().addLoseWars(1);
                        war.getGuild2().getOnlineMembers().forEach(m -> TitleUtil.sendTitle(m.getPlayer(), 20, 100, 30, Util.fixColor(Util.replaceString(ImportantColor + "GRATULACJE")), Util.fixColor(Util.replaceString(MainColor + "Twoja gildia " + ImportantColor + "wygrala " + MainColor + "wojne"))));
                        war.getGuild1().getOnlineMembers().forEach(m -> TitleUtil.sendTitle(m.getPlayer(), 20, 100, 30, Util.fixColor(Util.replaceString(ImportantColor + "PRZYKRO NAM")), Util.fixColor(Util.replaceString(MainColor + "Twoja gildia " + ImportantColor + "przegrala " + MainColor + "wojne"))));
                    } else {
                        war.getGuild1().addWinWars(1);
                        war.getGuild2().addLoseWars(1);
                        war.getGuild1().getOnlineMembers().forEach(m -> TitleUtil.sendTitle(m.getPlayer(), 20, 100, 30, Util.fixColor(Util.replaceString(ImportantColor + "GRATULACJE")), Util.fixColor(Util.replaceString(MainColor + "Twoja gildia " + ImportantColor + "wygrala " + MainColor + "wojne"))));
                        war.getGuild2().getOnlineMembers().forEach(m -> TitleUtil.sendTitle(m.getPlayer(), 20, 100, 30, Util.fixColor(Util.replaceString(ImportantColor + "PRZYKRO NAM")), Util.fixColor(Util.replaceString(MainColor + "Twoja gildia " + ImportantColor + "przegrala " + MainColor + "wojne"))));
                    }
                    WarManager.removeWar(war);
                }
                GuildManager.removeGuild(g);
                Bukkit.broadcastMessage(Util.fixColor(Util.replaceString(SpecialSigns + "->> " + MainColor + "Gildia " + SpecialSigns + "[" + ImportantColor + g.getTag() + SpecialSigns + "] " + ImportantColor + g.getName() + MainColor + " wygasla!")));
            }
        }
    }
}
