package pl.blackwater.core.events;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import pl.blackwater.core.managers.EasyScoreboardManager;
import pl.blackwater.core.managers.UserManager;
import pl.blackwaterapi.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
public class CustomEventManager {


    @NonNull
    @Getter
    private static HashMap<UUID, Integer> statCount;
    @Getter
    private static List<UUID> uuidEventList;

    @Getter
    private static CustomEvent activeEvent;

    static {
        statCount = new HashMap<>();
        uuidEventList = new ArrayList<>();
        activeEvent = null;
    }

    public static void compareEventStats(){
        uuidEventList.sort(new EventStatComparator());
    }


    public static void startEvent(EventType type, long time){
        if(type.equals(EventType.KILLS)){
            activeEvent =  new KillCustomEvent(time);
            statCount.clear();
            uuidEventList.clear();
            for(Player player : Bukkit.getOnlinePlayers()){
                EasyScoreboardManager.removeScoreBoard(player);
                EasyScoreboardManager.createScoreBoard(player);
            }
        }
        if(type.equals(EventType.BREAK_STONE)){
            activeEvent =  new BreakStoneCustomEvent(time);
            statCount.clear();
            uuidEventList.clear();
            for(Player player : Bukkit.getOnlinePlayers()){
                EasyScoreboardManager.removeScoreBoard(player);
                EasyScoreboardManager.createScoreBoard(player);
            }
        }
        if(type.equals(EventType.OPEN_CHEST)){
            activeEvent =  new OpenChestCustomEvent(time);
            statCount.clear();
            uuidEventList.clear();
            for(Player player : Bukkit.getOnlinePlayers()){
                EasyScoreboardManager.removeScoreBoard(player);
                EasyScoreboardManager.createScoreBoard(player);
            }
        }
        if(type.equals(EventType.PUMPKIN)){
            activeEvent = new PumpkinCustomEvent(time);
            statCount.clear();
            uuidEventList.clear();
            for(Player player : Bukkit.getOnlinePlayers()){
                EasyScoreboardManager.removeScoreBoard(player);
                EasyScoreboardManager.createScoreBoard(player);
            }
        }
    }
    public static void endEvent(){
        if(activeEvent != null){
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("&d&lEVENT &8->> &d&n" + getActiveEvent().getEventName() + "&8 <<- &6&nEVENT")));
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("      &8->> &a&lWYGRAL: &c"  + getPlace(0))));
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("      &8->> &a2 MIEJSCE: &c"  + getPlace(1))));
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("      &8->> &e3 MIEJSCE: &c"  + getPlace(2))));
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("      &8->> &b4 MIEJSCE: &c"  + getPlace(3))));
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("      &8->> &45 MIEJSCE: &c"  + getPlace(4))));
            Bukkit.broadcastMessage(Util.fixColor(Util.replaceString("&d&lEVENT &8->> &d&n" + getActiveEvent().getEventName() + "&8 <<- &6&nEVENT")));
            //TODO win

            for(Player player : Bukkit.getOnlinePlayers()){
                MinecraftServer.getServer().postToMainThread(() -> {
                    EasyScoreboardManager.removeScoreBoard(player);
                    player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                    EasyScoreboardManager.createScoreBoard(player);
                });
            }
        }



        statCount.clear();
        uuidEventList.clear();
        activeEvent = null;
    }
    public static String getPlace(int i){
        if(getUuidEventList().size() >= i+1){
            return UserManager.getUser(getUuidEventList().get(i)).getLastName();
        }else{
            return "BRAK";
        }
    }
    public static int getPlace(UUID uuid)
    {
        for (int num = 0; num < uuidEventList.size(); num++) {
            if (uuidEventList.get(num).equals(uuid)) {
                return num + 1;
            }
        }
        return 0;
    }


}
