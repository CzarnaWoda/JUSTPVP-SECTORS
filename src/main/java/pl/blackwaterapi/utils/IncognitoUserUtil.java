package pl.blackwaterapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.blackwater.core.Core;
import pl.blackwaterapi.incognitothreads.IncognitoActionType;
import pl.blackwaterapi.incognitothreads.IndependentThread;
import pl.blackwaterapi.objects.IncognitoUser;

import java.util.ArrayList;
import java.util.Collection;

public class IncognitoUserUtil {
    private static Collection<IncognitoUser> list = new ArrayList<>();

    public static void join(PlayerJoinEvent e){
        final Player player = e.getPlayer();
        IncognitoUser user = get(player);
        user.setChangeNick(player.getName());
        NameUtil.getInstance().updateJoin(player);
        Bukkit.getScheduler().runTaskLaterAsynchronously(Core.getPlugin(), () -> {
            if(player == null || !player.isOnline()) return;
            IndependentThread.action(IncognitoActionType.UPDATE_JOIN, player);

        }, 20);
    }
    public static void addIncognitoUser(IncognitoUser IncognitoUser){
        if(!getList().contains(IncognitoUser)) getList().add(IncognitoUser);
    }

    public static void removeIncognitoUser(IncognitoUser IncognitoUser){
        getList().remove(IncognitoUser);
    }

    public static Collection<IncognitoUser> getList(){
        return list;
    }

    public static IncognitoUser get(Player pp){
        return getByNick(pp.getName());
    }
    public static IncognitoUser getByNick(String name) {
        for(IncognitoUser IncognitoUser : getList()) {
            if(IncognitoUser.getName().equalsIgnoreCase(name)) return IncognitoUser;
        }
        return new IncognitoUser(name);
    }


}