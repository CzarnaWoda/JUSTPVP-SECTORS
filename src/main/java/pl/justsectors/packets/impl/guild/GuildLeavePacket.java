package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class GuildLeavePacket extends RedisPacket {

    private String guildTag;
    private UUID leaveUUID;
    private String leaveName;

    public GuildLeavePacket(String guildTag, UUID leaveUUID, String leaveName) {
        this.guildTag = guildTag;
        this.leaveUUID = leaveUUID;
        this.leaveName = leaveName;
    }

    public GuildLeavePacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getLeaveUUID() {
        return leaveUUID;
    }

    public String getLeaveName() {
        return leaveName;
    }
}
