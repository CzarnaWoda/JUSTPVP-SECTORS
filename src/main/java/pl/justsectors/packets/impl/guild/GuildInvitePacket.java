package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class GuildInvitePacket extends RedisPacket {

    private String guildTag;
    private String executorName;
    private String invited;
    private UUID invitedUUID;

    public GuildInvitePacket(String guildTag, String executorName, String invited, UUID invitedUUID) {
        this.guildTag = guildTag;
        this.executorName = executorName;
        this.invited = invited;
        this.invitedUUID = invitedUUID;
    }

    public GuildInvitePacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getInvitedUUID() {
        return invitedUUID;
    }

    public String getExecutorName() {
        return executorName;
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getInvited() {
        return invited;
    }
}
