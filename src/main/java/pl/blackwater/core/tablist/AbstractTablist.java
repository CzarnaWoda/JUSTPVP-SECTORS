package pl.blackwater.core.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractTablist
{
    private static final Set<AbstractTablist> TABLIST_CACHE;
    protected final TablistVariablesParser variables;
    protected final Map<Integer, String> tablistPattern;
    protected final String header;
    protected final String footer;
    protected final UUID player;
    protected final int ping;
    protected boolean firstPacket;

    public AbstractTablist(final Map<Integer, String> tablistPattern, final String header, final String footer, final int ping, final Player player) {
        this.variables = new TablistVariablesParser();
        this.firstPacket = true;
        this.tablistPattern = tablistPattern;
        this.header = header;
        this.footer = footer;
        this.ping = ping;
        this.player = player.getUniqueId();
        DefaultTablistVariables.install(this.variables);
    }

    public static void wipeCache() {
        AbstractTablist.TABLIST_CACHE.clear();
    }

    public static AbstractTablist createTablist(final Map<Integer, String> pattern, final String header, final String footer, final int ping, final Player player) {
        for (final AbstractTablist tablist : AbstractTablist.TABLIST_CACHE) {
            if (tablist.player.equals(player.getUniqueId())) {
                return tablist;
            }
        }
        final String fixedVersion = Reflections.getFixedVersion();
        switch (fixedVersion) {
            case "v1_8_R1":
            case "v1_8_R2":
            case "v1_8_R3": {
                final AbstractTablist tablist2 = new TablistImpl(pattern, header, footer, ping, player);
                AbstractTablist.TABLIST_CACHE.add(tablist2);
                return tablist2;
            }
            default: {
                throw new RuntimeException("Could not find tablist for given version.");
            }
        }
    }

    public static AbstractTablist getTablist(final Player player) {
        for (final AbstractTablist tablist : AbstractTablist.TABLIST_CACHE) {
            if (tablist.player.equals(player.getUniqueId())) {
                return tablist;
            }
        }
        throw new IllegalStateException("Given player's tablist does not exist!");
    }

    public static void removeTablist(final Player player) {
        for (final AbstractTablist tablist : AbstractTablist.TABLIST_CACHE) {
            if (tablist.player.equals(player.getUniqueId())) {
                AbstractTablist.TABLIST_CACHE.remove(tablist);
                break;
            }
        }
    }

    public static boolean hasTablist(final Player player) {
        for (final AbstractTablist tablist : AbstractTablist.TABLIST_CACHE) {
            if (tablist.player.equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public abstract void send();

    protected void sendPackets(final List<Object> packets) {
        final Player target = Bukkit.getPlayer(this.player);
        if (target == null) {
            return;
        }
        PacketSender.sendPacket(target, packets);
    }

    protected Object createBaseComponent(final String text, final boolean keepNewLines) {
        return NotificationUtil.createBaseComponent(text, keepNewLines);
    }

    protected String putVars(final String cell) {
        final User user = UserManager.getUser(player);
        if (user == null) {
            throw new IllegalStateException("Given player is null!");
        }
        final VariableParsingResult result = this.variables.createResultFor(user);
        String formatted = result.replaceInString(cell);
        formatted = StringUtils.colored(formatted);
        final String temp = Parser.parsePlace(formatted);
        if (temp != null) {
            formatted = temp;
        }
        return formatted;
    }

    @Deprecated
    protected String putHeaderFooterVars(final String text) {
        final Calendar time = Calendar.getInstance();
        final int hour = time.get(11);
        final int minute = time.get(12);
        final int second = time.get(13);
        String formatted = StringUtils.replace(text, "{HOUR}", ((hour < 10) ? "0" : "") + hour);
        formatted = StringUtils.replace(formatted, "{MINUTE}", ((minute < 10) ? "0" : "") + minute);
        formatted = StringUtils.replace(formatted, "{SECOND}", ((second < 10) ? "0" : "") + second);
        formatted = StringUtils.colored(formatted);
        return formatted;
    }

    protected boolean shouldUseHeaderAndFooter() {
        return !this.header.isEmpty() || !this.footer.isEmpty();
    }

    static {
        TABLIST_CACHE = ConcurrentHashMap.newKeySet();
    }
}
