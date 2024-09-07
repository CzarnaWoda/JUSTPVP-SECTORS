package pl.blackwater.guilds.data;

import lombok.Data;
import pl.blackwater.core.Core;
import pl.blackwater.core.settings.SectorConfig;
import pl.blackwater.guilds.managers.GuildManager;
import pl.blackwater.guilds.settings.GuildConfig;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.sectors.SectorManager;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class War implements Entry{

    private Guild guild1,guild2;
    private int kills1,kills2;
    private long startTime,endTime;
    private boolean start;
    private String sectorName;

    public War(Guild guild1, Guild guild2, long endTime){
        this.guild1 = guild1;
        this.kills1 = 0;
        this.guild2 = guild2;
        this.kills2 = 0;
        this.startTime = Util.parseDateDiff(GuildConfig.WARMANAGER_PREPARETIME,true);
        this.endTime = endTime;
        this.start = false;
        this.sectorName = SectorManager.getCurrentSector().get().getSectorName();
        //id,guild1,guild2,kills1,kills2,deaths1,deaths2,warKills,startTime,endTime
        //getGuild1(),getGuild2(),getKills1(),getKills2(),getDeaths1(),getDeaths2(),getWarKills(),getStartTime(),getEndTime()
    }


    @Override
    public void insert() {
        RedisChannel.INSTANCE.WARS.addAsync(GsonUtil.toJson(this));
    }

    @Override
    public void update(boolean b) {
        insert();
    }

    @Override
    public void delete() {
        RedisChannel.INSTANCE.WARS.removeAsync(GsonUtil.toJson(this));
    }

    public void addKills1(int kills){
        kills1 += kills;
        update(false);
    }
    public void addKills2(int kills){
        kills2 += kills;
        update(false);
    }

}
