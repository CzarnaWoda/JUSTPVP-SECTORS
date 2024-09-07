package pl.blackwater.core.managers;

import lombok.Getter;


public class ServerStatsManager {

    @Getter
    public static int globalKills;

    public static void addKills(int kills){
        globalKills += kills;
    }
}
