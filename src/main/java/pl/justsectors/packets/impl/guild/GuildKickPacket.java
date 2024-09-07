package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class GuildKickPacket extends RedisPacket {

    private String guildTag;
    private UUID kickedUUID;
    private String kickedName;
    private String executorName;


    public GuildKickPacket(String guildTag, UUID kickedUUID, String kickedName, String executorName) {
        this.guildTag = guildTag;
        this.kickedUUID = kickedUUID;
        this.kickedName = kickedName;
        this.executorName = executorName;
    }

    public GuildKickPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getKickedUUID() {
        return kickedUUID;
    }

    public String getKickedName() {
        return kickedName;
    }

    public String getExecutorName() {
        return executorName;
    }
}
