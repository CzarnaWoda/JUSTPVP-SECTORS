package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class GuildJoinPacket extends RedisPacket {

    private String guildTag;
    private String invitedName;
    private UUID invitedUUID;

    public GuildJoinPacket(String guildTag, String invitedName, UUID invitedUUID) {
        this.guildTag = guildTag;
        this.invitedName = invitedName;
        this.invitedUUID = invitedUUID;
    }

    public GuildJoinPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getInvitedName() {
        return invitedName;
    }

    public UUID getInvitedUUID() {
        return invitedUUID;
    }
}
