package pl.blackwaterapi.utils;

import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PlayerUtil {

    public static int getPing(Player player){
        int ping = 0;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return ping;
    }
}
