package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class GuildSetOwnerPacket extends RedisPacket {

    private String guildTag;
    private UUID ownerUUID;
    private String ownerName;
    private String executorName;

    public GuildSetOwnerPacket(String guildTag, UUID ownerUUID, String ownerName, String executorName) {
        this.guildTag = guildTag;
        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
        this.executorName = executorName;
    }

    public GuildSetOwnerPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getExecutorName() {
        return executorName;
    }
}
