package pl.blackwater.core.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import lombok.Getter;
import lombok.Setter;
import pl.blackwater.core.Core;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.redis.channels.RedisChannel;

@Getter
@Setter
public class Mine implements Entry {
	private int index;
	private String name;
	private String type;
	private int cost;
	private Location location;
	private int level;
	private String mainRegion;
	private String stoneRegion;
	private int mineStoneLimit;
	private String guild;
	private Set<UUID> allowedMembers = new HashSet<>();

	public Mine(int index,String name,String type,int cost,Location loc, int level,String mainRegion,String stoneRegion,int mineStoneLimit){
		super();
		this.index = index;
		this.name = name;
		this.type = type;
		this.cost = cost;
		this.location = loc;
		this.level = level;
		this.mainRegion = mainRegion;
		this.stoneRegion = stoneRegion;
		this.mineStoneLimit = mineStoneLimit;
		this.guild = "";
	}

	@Override
	public void delete() {
		RedisChannel.INSTANCE.MINES.removeAsync(this.getIndex());
	}

	@Override
	public void insert() {
		RedisChannel.INSTANCE.MINES.putAsync(this.getIndex(), GsonUtil.toJson(this));
	}

	@Override
	public void update(boolean now) 
	{
		insert();
	}

	public ProtectedRegion getProtectedMainRegion(){
		System.out.println("Probuje pobrac region " + getMainRegion());
		ProtectedRegion mainRegion = Core.getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion(getMainRegion());
		System.out.println("Czy main region to null: " + mainRegion);
		return mainRegion;
	}
	public ProtectedRegion getProtectedStoneRegion(){
		ProtectedRegion stoneRegion = Core.getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion(getStoneRegion());
		return stoneRegion;
	}
	public void addPlayer(User u){
		this.allowedMembers.add(u.getUuid());
	}

	public void addPlayerToRegion(User u){
		getProtectedMainRegion().getMembers().addPlayer(u.getUuid());
		getProtectedStoneRegion().getMembers().addPlayer(u.getUuid());
	}
}
