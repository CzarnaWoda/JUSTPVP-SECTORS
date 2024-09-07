package pl.blackwater.chestpvpdrop.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.blackwater.core.Core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class DropFile
{
    private static Core plugin;
    private static FileConfiguration dropConfig;
    private static File dropConfigFile;
    
    @SuppressWarnings("deprecation")
	public static void reloadConfig() {
        if (DropFile.dropConfigFile == null) {
            DropFile.dropConfigFile = new File(DropFile.plugin.getDataFolder(), "drops.yml");
        }
        DropFile.dropConfig = YamlConfiguration.loadConfiguration(DropFile.dropConfigFile);
        final InputStream defConfigStream = DropFile.plugin.getResource("drops.yml");
        if (defConfigStream != null) {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            DropFile.dropConfig.setDefaults(defConfig);
        }
    }
    
    public static FileConfiguration getConfig() {
        if (DropFile.dropConfig == null) {
            reloadConfig();
        }
        return DropFile.dropConfig;
    }
    
    public static void saveConfig() {
        if (DropFile.dropConfig == null || DropFile.dropConfigFile == null) {
            return;
        }
        try {
            getConfig().save(DropFile.dropConfigFile);
        }
        catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + DropFile.dropConfigFile, ex);
        }
    }
    
    public static void saveDefaultConfig() {
        if (DropFile.dropConfigFile == null) {
            DropFile.dropConfigFile = new File(DropFile.plugin.getDataFolder(), "drops.yml");
        }
        if (!DropFile.dropConfigFile.exists()) {
            DropFile.plugin.saveResource("drops.yml", false);
        }
    }
    
    static {
        DropFile.plugin = Core.getPlugin();
        DropFile.dropConfig = null;
        DropFile.dropConfigFile = null;
    }
}