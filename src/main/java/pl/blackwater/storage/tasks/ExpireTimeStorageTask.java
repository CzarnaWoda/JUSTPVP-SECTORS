package pl.blackwater.storage.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwater.core.managers.UserManager;
import pl.blackwater.storage.data.StorageRent;
import pl.blackwater.storage.managers.StorageRentManager;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.storage.StorageLeftPacket;
import pl.justsectors.redis.client.RedisClient;
import pl.justsectors.sectors.SectorManager;
import pl.justsectors.sectors.SectorType;

public class ExpireTimeStorageTask extends BukkitRunnable implements Colors{
	
	
	public void run() {
		if(SectorManager.getCurrentSector().get().getSectorType().equals(SectorType.STORAGE)){
		for(StorageRent storage : StorageRentManager.getStoragerents().values()) {
			if(storage.getExpireTime() != 0L && storage.getExpireTime() < System.currentTimeMillis()) {
				final StorageLeftPacket packet = new StorageLeftPacket(storage.getOwner(), storage.getId());
				RedisClient.sendSectorsPacket(packet);
			}
			}
		}
	}

}
