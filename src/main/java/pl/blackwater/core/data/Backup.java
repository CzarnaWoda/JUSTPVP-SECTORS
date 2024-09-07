package pl.blackwater.core.data;

import lombok.Data;
import org.bukkit.inventory.ItemStack;
import pl.blackwater.core.Core;
import pl.blackwater.core.enums.BackupType;
import pl.blackwater.market.utils.ItemStackUtil;
import pl.blackwaterapi.store.Entry;
import pl.blackwaterapi.utils.Base64Util;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.redis.channels.RedisChannel;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;

@Data
public class Backup implements Entry {

    private UUID backupUUID;
    private UUID owner;
    private transient ItemStack[] armor,inventory;
    private String armorString;
    private String inventoryString;
    private long backupTime;
    private long takeBackupTime;
    private BackupType backupType;
    private String backupCreator;

    public Backup(UUID backupUUID, UUID owner, ItemStack[] armor, ItemStack[] inventory, long backupTime, long takeBackupTime, BackupType backupType, String backupCreator){
        this.backupUUID = backupUUID;
        this.owner = owner;
        this.armor = armor;
        this.inventory = inventory;
        this.backupTime = backupTime;
        this.takeBackupTime =  takeBackupTime;
        this.backupType = backupType;
        this.backupCreator = backupCreator;
        this.armorString = Base64Util.itemStackArrayToBase64(this.armor);
        this.inventoryString = Base64Util.itemStackArrayToBase64(this.inventory);
    }

    public void setup(){
        try {
            this.armor = Base64Util.itemStackArrayFromBase64(this.armorString);
            this.inventory = Base64Util.itemStackArrayFromBase64(this.inventoryString);
        }catch (Exception e)
        {
            Core.getPlugin().getLogger().log(Level.INFO, "Inventory backupa " + this.backupUUID + " nie zaladowalo sie poprawnie!");
            e.printStackTrace();
        }
    }

    @Override
    public void insert() {
        RedisChannel.INSTANCE.BACKUPS.putAsync(this.backupUUID, GsonUtil.toJson(this));
    }

    @Override
    public void update(boolean b) {
        insert();
    }

    @Override
    public void delete()  {
        RedisChannel.INSTANCE.BACKUPS.removeAsync(this.backupUUID);
    }
}
