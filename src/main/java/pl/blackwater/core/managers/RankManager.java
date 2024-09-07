package pl.blackwater.core.managers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Rank;
import pl.blackwater.core.data.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RankManager {

    @Getter
    public HashMap<UUID, PermissionAttachment> permissions = new HashMap<>();
    @Getter
    private HashMap<String, Rank> ranks = new HashMap<>();

    public void joinPlayer(@NotNull Player player){
        if(permissions.get(player.getUniqueId()) == null) {
            PermissionAttachment attachment = player.addAttachment(Core.getPlugin());
            getPermissions().put(player.getUniqueId(), attachment);
        }else{
            permissions.remove(player.getUniqueId());
            PermissionAttachment attachment = player.addAttachment(Core.getPlugin());
            getPermissions().put(player.getUniqueId(), attachment);
        }
    }
    public Rank getRank(String name){
        for(Rank r : getRanks().values()){
            if(r.getName().equalsIgnoreCase(name)){
                return r;
            }
        }
        return null;
    }
    public void implementPermissions(@NotNull User u){
        Rank rank = u.getUserRank();
        if(u.getPlayer() != null){
            PermissionAttachment permissionAttachment = getPermissions().get(u.getPlayer().getUniqueId());
            for(String permissions : rank.getPermissions()){
                permissionAttachment.setPermission(permissions, true);
            }
        }
    }
    public void unimplementPermissions(@NotNull User u){
        Rank rank = u.getUserRank();
        if(u.getPlayer() != null){
            PermissionAttachment permissionAttachment = getPermissions().get(u.getPlayer().getUniqueId());
            if(permissionAttachment != null) {
                for (String permissions : rank.getPermissions()) {
                    if (permissionAttachment.getPermissions().containsKey(permissions)) {
                        permissionAttachment.unsetPermission(permissions);
                    }
                }
            }
        }
    }
    public Rank createRank(String rank){
        Rank r = new Rank(rank, "CHANGE","CHANGE",new ArrayList<>());
        ranks.put(rank, r);
        r.update(true);
        return r;
    }
}
