package pl.blackwater.core.tablist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pl.blackwater.core.data.User;
import pl.blackwater.core.enums.TabListType;
import pl.blackwater.core.managers.EventManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.settings.CoreConfig;
import pl.blackwater.core.tasks.TabUpdateTask;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.managers.MemberManager;
import pl.blackwater.market.managers.AuctionManager;
import pl.blackwaterapi.utils.PlayerUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.function.Consumer;

public final class DefaultTablistVariables
{
    private static final Collection<Consumer<TablistVariablesParser>> installers;

    private static int getOnline(){
        int online = 0;
        for(Sector sector : SectorManager.getSectors().values()){
            online = online + sector.getOnlinePlayers().size();
        }
        return online;
    }
    public static void install(final TablistVariablesParser parser) {
        parser.add(new TimeFormattedVariable("HOUR", user -> Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        parser.add(new TimeFormattedVariable("MINUTE", user -> Calendar.getInstance().get(Calendar.MINUTE)));
        parser.add(new TimeFormattedVariable("SECOND", user -> Calendar.getInstance().get(Calendar.SECOND)));
        parser.add(new SimpleTablistVariable("PLAYER", User::getLastName));
        parser.add(new SimpleTablistVariable("KILLS", user -> String.valueOf(user.getKills())));
        parser.add(new SimpleTablistVariable("DEATHS", user -> String.valueOf(user.getDeaths())));
        parser.add(new SimpleTablistVariable("KILLSTREAK", user -> String.valueOf(user.getKillStreak())));
        parser.add(new SimpleTablistVariable("LOGOUTS", user -> String.valueOf(user.getLogouts())));
        parser.add(new SimpleTablistVariable("KDR", user -> String.valueOf(user.getKd())));
        parser.add(new SimpleTablistVariable("LEVEL", user -> String.valueOf(user.getLevel())));
        parser.add(new SimpleTablistVariable("MONEY", user -> String.valueOf(user.getMoney())));
        parser.add(new GuildDependentTablistVariable("G-TAG", user -> user.getGuild().getTag(),user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-POINTS", user -> String.valueOf(user.getGuild().getPoints()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-KILLS", user -> String.valueOf(user.getGuild().getKills()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-DEATHS", user -> String.valueOf(user.getGuild().getDeaths()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-SOUL", user -> String.valueOf(user.getGuild().getGuildSoul()), user -> "BRAK"));
        parser.add(new GuildDependentTablistVariable("G-ONLINE", user -> "&a" + user.getGuild().getOnlineMembers().size() + "&8/&9" + user.getGuild().getGuildMembers().values().size(), user -> "BRAK"));
        parser.add(new SimpleTablistVariable("RANK", user -> user.getRank().toUpperCase()));
        parser.add(new SimpleTablistVariable("PING", user -> PlayerUtil.getPing(user.getPlayer()) + "ms"));
        parser.add(new SimpleTablistVariable("TIMEFROMJOIN", User::getOnlineTime));
        parser.add(new SimpleTablistVariable("P-ISTURBOMONEY", user -> Util.replaceString(user.getEventTurboMoney() == 2 || EventManager.isOnTurboMoney() ? ChatColor.GREEN + "%V%" : ChatColor.RED + "%X%")));
        parser.add(new SimpleTablistVariable("P-ISTURBODROP", user -> Util.replaceString(user.isTurboDrop() || EventManager.isOnTurboDrop() ? ChatColor.GREEN + "%V%" : ChatColor.RED + "%X%")));
        parser.add(new SimpleTablistVariable("P-ISTURBOEXP", user -> Util.replaceString(user.isTurboExp() || EventManager.isOnTurboExp() ? ChatColor.GREEN + "%V%" : ChatColor.RED + "%X%")));
        parser.add(new SimpleTablistVariable("ISEVENT", user -> Util.replaceString(EventManager.isOnEventDrop() || EventManager.isOnEventExp() ? ChatColor.GREEN + "%V%" : ChatColor.RED + "%X%")));
        parser.add(new SimpleTablistVariable("ONLINE", user -> user.getUserRank().getPermissions().contains("core.online.root") ? String.valueOf(getOnline()) : "XXX"));
        parser.add(new SimpleTablistVariable("REGISTERPLAYERS", user -> String.valueOf(UserManager.getUsers().size())));
        parser.add(new SimpleTablistVariable("SECTOR", user -> String.valueOf(SectorManager.getCurrentSector().get().getSectorName())));
        //parser.add(new SimpleTablistVariable("BANSIZE", user -> String.valueOf(BanManager.getBans().size() + BanIPManager.getBansIp().size())));
        parser.add(new SimpleTablistVariable("AUCTIONSIZE", user -> String.valueOf(AuctionManager.getAuctions().size())));
        parser.add(new SimpleTablistVariable("TPS", user -> Util.replaceString(ChatColor.GREEN + "%V%")));
        parser.add(new SimpleTablistVariable("PVP", user -> Util.replaceString(user.getPlayer().getWorld().getPVP() ? ChatColor.GREEN + "%V%" : ChatColor.RED + "%X%")));
        parser.add(new SimpleTablistVariable("TABLISTTYPE", user -> TabUpdateTask.type.equals(TabListType.KILLS) ? "ZABOJSTW" : TabUpdateTask.type.equals(TabListType.DEATHS) ? "SMIERCI" : "WYKOPANEGO KAMIENIA"));

        for (final Consumer<TablistVariablesParser> installer : DefaultTablistVariables.installers) {
            installer.accept(parser);
        }
    }

    public static void registerInstaller(final Consumer<TablistVariablesParser> parser) {
        DefaultTablistVariables.installers.add(parser);
    }

    static {
        installers = new ArrayList<>();
    }
}
