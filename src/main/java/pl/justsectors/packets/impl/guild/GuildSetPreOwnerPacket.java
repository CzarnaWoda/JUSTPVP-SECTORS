package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class GuildSetPreOwnerPacket extends RedisPacket {

    private String guildTag;
    private String newOwnerName;
    private UUID newOwnerUUID;
    private String executorName;

    public GuildSetPreOwnerPacket(String guildTag, String newOwnerName, UUID newOwnerUUID, String executorName) {
        this.guildTag = guildTag;
        this.newOwnerName = newOwnerName;
        this.newOwnerUUID = newOwnerUUID;
        this.executorName = executorName;
    }

    public GuildSetPreOwnerPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getNewOwnerName() {
        return newOwnerName;
    }

    public UUID getNewOwnerUUID() {
        return newOwnerUUID;
    }

    public String getExecutorName() {
        return executorName;
    }
}
