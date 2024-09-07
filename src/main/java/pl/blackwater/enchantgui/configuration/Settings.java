package pl.blackwater.enchantgui.configuration;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.enchantgui.data.EnchantmentSettings;
import pl.blackwater.enchantgui.data.LevelSettings;
import pl.blackwater.enchantgui.utils.Enchantments;
import pl.blackwaterapi.configs.ConfigCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings extends ConfigCreator {

    public static int firstRows;
    public static String firstTitle;
    public static List<String> firstLores;

    public static int secondRows;
    public static String secondTitle;
    public static Map<Integer, EnchantmentSettings> secondEnchantmentSettings;

    public static int thirdRows;
    public static String thirdTitle;
    public static Map<Integer, LevelSettings> thirdLevelSettings;

    public static String closed;
    public static String success;
    public static String noMoney;
    public static String enchantsize;
    public static double vipDiscount;

    public Settings () {
        super("enchantshops.yml", "eeee", Core.getPlugin());
        FileConfiguration config = getConfig();
        enchantsize = config.getString("enchantsize");
        closed = config.getString("closed");
        success = config.getString("success");
        noMoney = config.getString("no-money");
        vipDiscount = config.getDouble("vip-discount");

        firstRows = config.getInt("gui.1.rows");
        firstTitle = config.getString("gui.1.title");
        firstLores = config.getStringList("gui.1.lores");

        secondRows = config.getInt("gui.2.rows");
        secondTitle = config.getString("gui.2.title");
        secondEnchantmentSettings = new HashMap<>();
        int index = 0;
        for (String key : config.getConfigurationSection("gui.2.enchantments").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("gui.2.enchantments." + key);

            Material item = Material.matchMaterial(section.getString("item"));
            ItemStack itemStack = new ItemStack(item);
            String name = section.getString("name");
            String lore = section.getString("lore");

            EnchantmentSettings enchantmentSettings = new EnchantmentSettings(key, itemStack, name, lore);
            secondEnchantmentSettings.put(index, enchantmentSettings);
            index++;
        }
        index = 0;

        thirdRows = config.getInt("gui.3.rows");
        thirdTitle = config.getString("gui.3.title");
        thirdLevelSettings = new HashMap<>();
        for (String key : config.getConfigurationSection("gui.3.levels").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("gui.3.levels." + key);

            Enchantment enchantment = Enchantments.enchants.get(section.getString("enchantment").toLowerCase());
            Material item = Material.matchMaterial(section.getString("item"));
            ItemStack itemStack = new ItemStack(item);
            int levels = section.getInt("levels");
            String name = section.getString("name");
            String lore = section.getString("lore");
            int price = section.getInt("price");

            LevelSettings levelSettings = new LevelSettings(key, enchantment, itemStack, levels, 1, name, lore, price);
            thirdLevelSettings.put(index, levelSettings);
            index++;
        }
    }
}
