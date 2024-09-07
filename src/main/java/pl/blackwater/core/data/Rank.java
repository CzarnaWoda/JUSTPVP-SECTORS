package pl.blackwater.core.data;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import pl.blackwater.core.Core;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.store.Entry;
import java.util.List;

@Data
public class Rank implements Entry {

    @NonNull
    public String name,prefix,suffix;
    @NonNull
    public List<String> permissions;
    @Override
    public void insert() {

    }

    @Override
    public void update(boolean b) {
        Core.getRankConfig().setField("ranks." + name + ".permissions", getPermissions());
        Core.getRankConfig().setField("ranks." + name + ".prefix", getPrefix());
        Core.getRankConfig().setField("ranks." + name + ".suffix", getSuffix());
        System.out.println("RankInfo - Rank update status = " + b + ", rank name: " + name);
    }
    @Override
    public void delete() {
        Core.getRankConfig().getConfig().set("ranks. " + name, null);
    }
    public void addPermission(String pex){
        if(permissions.contains(pex)){
            System.out.println("RankERROR - permission alredy set, cant be add ! (pl.blackwater.hardcore.data.Rank -> addPermission(String pex))");
            return;
        }
        permissions.add(pex);
        for(User user : UserManager.getUsers().values()) {
            if (user.getRank().equalsIgnoreCase(getName())) {
                if (user.getPlayer() != null) {
                    Player player = user.getPlayer();
                    PermissionAttachment pperms = Core.getRankManager().getPermissions().get(player.getUniqueId());
                    pperms.setPermission(pex, true);
                }
            }
        }
    }
    public void removePermission(String pex){
        if(!permissions.contains(pex)){
            System.out.println("RankERROR - permission not found, cant be remove ! (pl.blackwater.hardcore.data.Rank -> removePermission(String pex))");
            return;
        }
        permissions.remove(pex);
        for(User user : UserManager.getUsers().values()) {
            if (user.getRank().equalsIgnoreCase(getName())) {
                if (user.getPlayer() != null) {
                    Player player = user.getPlayer();
                    PermissionAttachment pperms = Core.getRankManager().getPermissions().get(player.getUniqueId());
                    pperms.unsetPermission(pex);
                }
            }
        }
    }
}
