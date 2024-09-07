package pl.blackwater.core.settings;

import pl.blackwater.core.Core;
import pl.blackwaterapi.configs.ConfigCreator;

public class BackupConfig extends ConfigCreator {

    public static int BACKUP_AUTOMATIC_DELAY;
    public BackupConfig() {
        super("backup.yml", "backup config", Core.getPlugin());
        BACKUP_AUTOMATIC_DELAY = getConfig().getInt("backup.automatic.delay");
    }
}
