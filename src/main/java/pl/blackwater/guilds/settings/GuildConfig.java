package pl.blackwater.guilds.settings;

import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;
import pl.blackwaterapi.utils.Logger;

import java.lang.reflect.Field;

public class GuildConfig extends ConfigCreator {
    public static String COST_CREATE_NORMAL;
    public static String COST_CREATE_VIP;
    public static boolean COST_CREATE_LVL,ENABLEMANAGER_WAR;
    public static String COST_LEADER_NORMAL;
    public static String COST_LIMIT_NORMAL;
    public static String COST_EXPIRE_NORMAL;
    public static String WARMANAGER_PREPARETIME,WARMANAGER_WARTIME;
    public static long GUILDS_CREATE_TIME;
    public static double MULTIPLER;
    public static String TAG_HEADADMIN_SUFFIX,TAG_ADMIN_SUFFIX,TAG_MODERATOR_SUFFIX,TAG_HELPER_SUFFIX,TAG_SVIP_SUFFIX,TAG_VIP_SUFFIX;
    public static int WARMANAGER_PRIZEAMOUNT;

    public GuildConfig() {
        super("guildconfig.yml", "Cfg", Core.getPlugin());

        final FileConfiguration config = getConfig();
        GUILDS_CREATE_TIME = config.getLong("guilds.create.time");
        COST_CREATE_NORMAL = config.getString("cost.create.normal");
        COST_CREATE_VIP = config.getString("cost.create.vip");
        COST_CREATE_LVL = config.getBoolean("cost.create.lvl");
        COST_LEADER_NORMAL = config.getString("cost.leader.normal");
        COST_LIMIT_NORMAL = config.getString("cost.limit.normal");
        COST_EXPIRE_NORMAL = config.getString("cost.expire.normal");
        MULTIPLER = 1.5;
        TAG_HEADADMIN_SUFFIX = config.getString("tag.headadmin.suffix");
        TAG_ADMIN_SUFFIX = config.getString("tag.admin.suffix");
        TAG_MODERATOR_SUFFIX = config.getString("tag.moderator.suffix");
        TAG_HELPER_SUFFIX = config.getString("tag.helper.suffix");
        TAG_SVIP_SUFFIX = config.getString("tag.svip.suffix");
        TAG_VIP_SUFFIX = config.getString("tag.vip.suffix");
        WARMANAGER_PREPARETIME = config.getString("warmanager.preparetime");
        WARMANAGER_WARTIME = config.getString("warmanager.wartime");
        WARMANAGER_PRIZEAMOUNT = config.getInt("warmanager.prizeamount");
        ENABLEMANAGER_WAR  = config.getBoolean("enablemanager.war");
    }

    public static void setGuildCreate(long arg)
    {
        GUILDS_CREATE_TIME = arg;
    }

}
