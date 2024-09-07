package pl.justsectors.packets.impl.backups;

import pl.blackwater.core.data.Backup;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class BackupCreatePacket extends RedisPacket {

    private Backup backup;

    public BackupCreatePacket(Backup backup) {
        this.backup = backup;
    }

    public BackupCreatePacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public Backup getBackup() {
        return backup;
    }
}
