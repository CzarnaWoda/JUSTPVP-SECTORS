package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

import java.util.UUID;

public class GuildCreatePacket extends RedisPacket {

    private String guildTag;
    private String guildName;
    private UUID ownerUUID;
    private String ownerName;


    public GuildCreatePacket(String guildTag, String guildName, UUID ownerUUID, String ownerName) {
        this.guildTag = guildTag;
        this.guildName = guildName;
        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
    }

    public GuildCreatePacket() {
    }


    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getGuildName() {
        return guildName;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public String getOwnerName() {
        return ownerName;
    }
}
