package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class GuildRemoveSoulsPacket extends RedisPacket {

    private String guildTag;
    private int soulsToRemove;

    public GuildRemoveSoulsPacket(String guildTag, int soulsToRemove) {
        this.guildTag = guildTag;
        this.soulsToRemove = soulsToRemove;
    }

    public GuildRemoveSoulsPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public int getSoulsToRemove() {
        return soulsToRemove;
    }
}
