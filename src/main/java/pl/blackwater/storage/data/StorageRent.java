package pl.blackwater.storage.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import lombok.Data;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.core.utils.LocationUtil;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.store.Setupable;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.redis.channels.RedisChannel;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;

@Data
public class StorageRent implements Entry, Setupable {
	private int id,cost;
	private UUID owner;
	private Location signLocation;
	private long rentTime,expireTime;
	private boolean rent;
	private String storageRegionName;
	private Location interactBlock;
	private String members;

	private transient ProtectedRegion storageRegion;
	private transient Sign sign;

	public StorageRent(final int id, final Sign sign, final long rentTime, final int cost, final ProtectedRegion storageRegion, Location interactBlock) {
		this.id = id;
		this.owner = null;
		this.sign = sign;
		this.rentTime = rentTime;
		this.expireTime = 0L;
		this.rent = false;
		this.cost = cost;
		this.storageRegion = storageRegion;
		this.storageRegionName = storageRegion.getId();
		this.signLocation = sign.getLocation();
		this.interactBlock = interactBlock;
		this.members = "";
	}
	public StorageRent(final int id, final Location sign, final long rentTime, final int cost, final String storageRegion, Location interactBlock) {
		this.id = id;
		this.owner = null;
		this.signLocation = sign;
		this.rentTime = rentTime;
		this.expireTime = 0L;
		this.rent = false;
		this.cost = cost;
		this.storageRegionName = storageRegion;
		this.interactBlock = interactBlock;
		this.members = "";
	}
	@Override
	public void setup()
	{
	}

	public ProtectedRegion getStorageRegion(){
		return Core.getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion(getStorageRegionName());
	}
	public void delete() {
		RedisChannel.INSTANCE.STORAGE.removeAsync(this.id);
	}

	public void insert() {
		RedisChannel.INSTANCE.STORAGE.putAsync(this.id, GsonUtil.toJson(this));
	}

	public void update(boolean arg0) {
		insert();
	}
	public List<String> getMembersList(){
		List<String> m = new ArrayList<>();
		for(String s : members.split(";")){
			m.add(s);
		}
		return m;
	}
	public void removeMember(String uuid){
		List<String> ms = getMembersList();
		ms.remove(uuid);
		String mm = "";
		for(String s : ms){
			mm = mm + s + ";";
		}
		this.members = mm;
	}
	public void addMember(String uuid){
		List<String> ms = getMembersList();
		ms.add(uuid);
		String mm = "";
		for(String s : ms){
			mm = mm + s + ";";
		}
		this.members = mm;
	}
	public Location getTeleportLocation() {
		return interactBlock.add(0.0, 1.5, 0.0);
	}
	
	public void addExpireTime(long time) {
		this.expireTime += time;
	}
	public Sign getSign() {
		return (Sign)Bukkit.getWorld("world").getBlockAt(signLocation.getBlockX(), signLocation.getBlockY(), signLocation.getBlockZ()).getState();
	}
	public Sign getReSign() {
		return (Sign)Bukkit.getWorld("world").getBlockAt(signLocation.getBlockX(), signLocation.getBlockY(), signLocation.getBlockZ()).getState();
	}
	
}
