package pl.blackwater.core.managers;

import lombok.Getter;
import org.bukkit.entity.Player;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Backup;
import pl.blackwater.core.enums.BackupType;
import pl.blackwaterapi.utils.GsonUtil;
import pl.blackwaterapi.utils.Logger;
import pl.justsectors.redis.channels.RedisChannel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class BackupManager {

    @Getter private ConcurrentHashMap<UUID, Backup> backups = new ConcurrentHashMap<>();

    public Backup createBackup(Player player, BackupType backupType, String creator){
        Backup backup = new Backup(UUID.randomUUID(), player.getUniqueId(), player.getInventory().getArmorContents(), player.getInventory().getContents(), System.currentTimeMillis(), 0L, backupType,creator);
        backups.put(backup.getBackupUUID(), backup);
        List<Backup> backupList = getBackups(backup.getOwner(), backup.getBackupType());
        List<Long> list = new ArrayList<>();
        if(backupList.size() > 5){
            for(Backup backup1 : backupList){
                list.add(backup1.getBackupTime());
            }
            Long min = list.stream().min(Comparator.comparing(Long::intValue)).get();
            for(Backup remove : backupList){
                if(remove.getBackupTime() == min){
                    deleteBackup(remove);
                }
            }
        }
        return backup;
    }

    public void registerBackup(final Backup backup){
        backups.put(backup.getBackupUUID(), backup);
        List<Backup> backupList = getBackups(backup.getOwner(), backup.getBackupType());
        List<Long> list = new ArrayList<>();
        if(backupList.size() > 5){
            for(Backup backup1 : backupList){
                list.add(backup1.getBackupTime());
            }
            Long min = list.stream().min(Comparator.comparing(Long::intValue)).get();
            for(Backup remove : backupList){
                if(remove.getBackupTime() == min){
                    deleteBackup(remove);
                }
            }
        }
    }


    public Backup getBackup(UUID BackupUUID){
        return backups.get(BackupUUID);
    }
    public List<Backup> getBackups(UUID playerUUID, BackupType backupType) {
        List<Backup> backupList = new ArrayList<>();
        for (Backup b : backups.values())
            if (b.getOwner().equals(playerUUID) && b.getBackupType().equals(backupType)) backupList.add(b);
        return backupList;
    }
    public void deleteBackup(Backup b){
        backups.remove(b.getBackupUUID());
        b.delete();
    }
    public void load(){
        RedisChannel.INSTANCE.BACKUPS.forEach(((uuid, s) -> {
            final Backup backup = GsonUtil.fromJson(s, Backup.class);
            backup.setup();
            backups.put(uuid, backup);
        }));
        Logger.info("Loaded " + backups.size() + " backups!");
    }
}
