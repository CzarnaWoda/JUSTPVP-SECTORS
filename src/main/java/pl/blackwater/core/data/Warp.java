package pl.blackwater.core.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;

import lombok.Data;
import pl.blackwater.core.Core;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.redis.channels.RedisChannel;

@Data
public class Warp
{
  private String name,pex;
  private Location location;
  
  public Warp(final String name,final Location location,final String pex)
  {
    this.name = name;
    this.location = location;
    this.pex = pex;
    insert();
  }
  
  private void insert()
  {
    RedisChannel.INSTANCE.WARPS.putAsync(this.name, GsonUtil.toJson(this));
  }

  public void delete()
  {
    RedisChannel.INSTANCE.WARPS.removeAsync(this.name);
  }
}
