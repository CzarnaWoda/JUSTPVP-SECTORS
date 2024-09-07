package pl.blackwater.core.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.blackwater.core.Core;
import pl.blackwater.core.data.Backup;
import pl.blackwater.core.enums.BackupType;
import pl.blackwater.core.interfaces.Colors;
import pl.blackwaterapi.utils.Util;
import pl.justsectors.packets.impl.backups.BackupCreatePacket;
import pl.justsectors.redis.client.RedisClient;

public class BackupTask extends BukkitRunnable implements Colors {
    @Override
    public void run() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            boolean hasPerm = online.hasPermission("core.admin");
            if(hasPerm)
            {
                Util.sendMsg(online, Util.fixColor(Util.replaceString(ImportantColor + "BACKUPMANAGER " + SpecialSigns + "->> " + WarningColor + "Trwa tworzenie automatycznego zapisu ekwipunku na serwerze!")));
            }
            Util.sendMsg(online, Util.replaceString(SpecialSigns + "->> " + MainColor + "Trwa " + ImportantColor + "zapisywanie twojego ekwipunku..."));
            final Backup backup = Core.getBackupManager().createBackup(online, BackupType.AUTOMATIC, "CONSOLE");
            backup.insert();
            RedisClient.sendSectorsPacket(new BackupCreatePacket(backup));
            Util.sendMsg(online, Util.replaceString(SpecialSigns + "->> " + MainColor + "Zapis twojego ekwipunku zostal pomyslnie " + ImportantColor + "ukonczony"));
            if(hasPerm)
            {
                Util.sendMsg(online,Util.fixColor(Util.replaceString(ImportantColor + "BACKUPMANAGER " + SpecialSigns + "->> " + WarningColor + "Zakonczono automatyczny zapis ekwipunku na serwerze!")));
            }
        }
    }
}
