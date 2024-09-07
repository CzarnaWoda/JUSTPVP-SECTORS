package pl.blackwater.storage.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import lombok.Getter;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.User;
import pl.blackwater.storage.data.StorageRent;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.redis.channels.RedisChannel;

public class StorageRentManager {
	
	@Getter private static final ConcurrentHashMap<Integer, StorageRent> storagerents = new ConcurrentHashMap<>();
	@Getter private static final HashMap<Player, StorageRent> addMembersAction = new HashMap<>();
	
	public static StorageRent addStorageRent(Sign sign, long rentTime, int cost, ProtectedRegion storageRegion, Block interactBlock) {
		final int id = storagerents.size() + 1;
		StorageRent storage = new StorageRent(id, sign, rentTime, cost, storageRegion, interactBlock.getLocation());
		storagerents.put(id, storage);
		return storage;
	}
	public static StorageRent addStorageRent(Location sign, long rentTime, int cost, String storageRegion, Location interactBlock) {
		final int id = storagerents.size() + 1;
		StorageRent storage = new StorageRent(id, sign, rentTime, cost, storageRegion, interactBlock);
		storagerents.put(id, storage);
		return storage;
	}
	
	public static StorageRent getStorageRent(int id) {
		return storagerents.get(id);
	}
	
	public static StorageRent getStorageRent(Sign sign) {
		for(StorageRent storage : storagerents.values()) {
			if(storage.getSign().equals(sign)) {
				return storage;
			}
		}
		return null;
	}
	public static StorageRent getFreeStorageRent() {
		for(StorageRent storage : storagerents.values()) {
			if(!storage.isRent()) {
				return storage;
			}
		}
		return null;
	}
	public static Set<StorageRent> getUserStorages(User u){
		final Set<StorageRent> storages = new HashSet<>();
		for(StorageRent storage : storagerents.values()) {
			if(storage.getOwner() != null && storage.getOwner().equals(u.getUuid())) {
				storages.add(storage);
			}
		}
		return storages;
	}
	
	public static void addFlagstoRegion(ProtectedRegion region) {
        region.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.ENDERPEARL, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.CREEPER_EXPLOSION, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.BUILD, StateFlag.State.ALLOW);
        region.setFlag(DefaultFlag.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region.setFlag(DefaultFlag.BLOCK_BREAK, StateFlag.State.ALLOW);
        region.setFlag(DefaultFlag.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region.setFlag(DefaultFlag.CHEST_ACCESS, StateFlag.State.ALLOW);
        region.setFlag(DefaultFlag.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region.setFlag(DefaultFlag.FIRE_SPREAD, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.LAVA_FIRE, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.GHAST_FIREBALL, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.INVINCIBILITY, StateFlag.State.ALLOW);
        region.setFlag(DefaultFlag.ICE_MELT, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.LIGHTER, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.LIGHTNING, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.MOB_DAMAGE, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.MOB_SPAWNING, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.SNOW_FALL, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.PLACE_VEHICLE, StateFlag.State.DENY);
        region.setFlag(DefaultFlag.POTION_SPLASH, StateFlag.State.DENY);
        region.setPriority(50);
	}

	public static void setup() {
		RedisChannel.INSTANCE.STORAGE.forEach(((integer, s) -> {
			final StorageRent storage = GsonUtil.fromJson(s, StorageRent.class);
			storage.setup();
			getStoragerents().put(storage.getId(), storage);
		}));
		Logger.info("Loaded " + getStoragerents().size() + " storages!");
	}
}
