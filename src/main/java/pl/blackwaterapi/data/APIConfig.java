package pl.blackwaterapi.data;

import org.bukkit.configuration.file.FileConfiguration;
import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;

import java.util.List;

public class APIConfig extends ConfigCreator {

    public static boolean ENABLED;
    public static String DATABASE_TABLEPREFIX;
    public static String DATABASE_MYSQL_HOST;
    public static int DATABASE_MYSQL_PORT;
    public static String DATABASE_MYSQL_USER;
    public static String DATABASE_MYSQL_PASS;
    public static String DATABASE_MYSQL_NAME;
    public static List<String> SUPERADMINSYSTEM_ADMINUUID;


    public APIConfig() {
        super("config.yml", "API CONFIG", Core.getPlugin());

        final FileConfiguration config = getConfig();

        DATABASE_TABLEPREFIX = config.getString("database.tableprefix");
        DATABASE_MYSQL_HOST = config.getString("database.mysql.host");
        DATABASE_MYSQL_PORT = config.getInt("database.mysql.port");
        DATABASE_MYSQL_USER = config.getString("database.mysql.user");
        DATABASE_MYSQL_PASS = config.getString("database.mysql.pass");
        DATABASE_MYSQL_NAME = config.getString("database.mysql.name");
        SUPERADMINSYSTEM_ADMINUUID = config.getStringList("superadminsystem.adminuuid");

    }
}
