package pl.blackwater.enchantgui;

import org.bukkit.entity.Player;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;


public class APIHandler {


    public static void addMoney(Player player, int money) {
        User user = UserManager.getUser(player);
        user.addMoneyViaPacket(money);
    }

    public static void removeMoney(Player player, int money) {
        User user = UserManager.getUser(player);
        user.removeMoneyViaPacket(money);
    }

    public static int getMoney(Player player) {
        User user = UserManager.getUser(player);
        return user.getMoney();
    }

    public static boolean hasMoney(Player player, int money) {
        return getMoney(player) >= money;
    }

    public static int getDifference(Player player, int money) {
        return money - getMoney(player);
    }


}
