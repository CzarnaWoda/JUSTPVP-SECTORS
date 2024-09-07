package pl.blackwater.enchantgui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.enchantgui.configuration.Settings;
import pl.blackwater.enchantgui.data.LevelSettings;

import java.util.*;

public class TemporaryStorage {

    public static Map<UUID, Storage> storageMap = new WeakHashMap<>();
    public static List<UUID> goingThrough = new ArrayList<>();

    public static ItemStack getItem(Player player) {
        return storageMap.get(player.getUniqueId()).getItem();
    }

    public static LevelSettings getLevelSettings(Player player) {
        return storageMap.get(player.getUniqueId()).getLevelSettings();
    }

    public static void setItem(Player player, ItemStack item) {
        Storage storage = new Storage(player.getUniqueId());
        storage.setItem(item);
        storageMap.put(player.getUniqueId(), storage);
    }

    public static void setLevelSettings(Player player, int slot) {
        LevelSettings levelSettings = Settings.thirdLevelSettings.get(slot);
        storageMap.get(player.getUniqueId()).setLevelSettings(levelSettings);
    }

    public static void close(Player player) {
        storageMap.remove(player.getUniqueId());
    }

}
