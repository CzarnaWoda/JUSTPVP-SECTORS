package pl.blackwater.enchantgui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.blackwater.enchantgui.commands.ShopCommand;
import pl.blackwater.enchantgui.configuration.Settings;
import pl.blackwater.enchantgui.listeners.InventoryClick;
import pl.blackwater.enchantgui.listeners.InventoryClose;
import pl.blackwater.enchantgui.listeners.InventoryDrag;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Logger logger;

    public static Main getInstance() {
        return instance;
    }

    public void onLoad() {
        instance = this;
        logger = getLogger();
    }

    public void onEnable() {
        long start = System.currentTimeMillis();
        logger.info("Loading resources...");
        logger.info("Loading configuration...");
        saveDefaultConfig();
        new Settings();
        logger.info("Registering listeners...");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new InventoryClose(), this);
        pm.registerEvents(new InventoryDrag(), this);
        logger.info("Registering commands...");
        getCommand("enchantshop").setExecutor(new ShopCommand());
        logger.info("Loading inventories (GUI)...");
        GUI.initSecond();
        long diff = System.currentTimeMillis() - start;
        double time = diff / 1000.0;
        logger.info("Finished loading! (" + time + "s)");
    }
}
