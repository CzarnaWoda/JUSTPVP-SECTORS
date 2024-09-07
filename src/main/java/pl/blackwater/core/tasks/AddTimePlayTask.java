package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;

public class AddTimePlayTask extends BukkitRunnable {
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            final User user = UserManager.getUser(player);

            user.addTimePlay(5);
        }
    }
}
