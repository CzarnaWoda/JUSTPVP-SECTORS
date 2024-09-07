package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class GuildSetPvPPacket extends RedisPacket {

    private String guildTag;
    private String executorName;

    public GuildSetPvPPacket(String guildTag, String executorName) {
        this.guildTag = guildTag;
        this.executorName = executorName;
    }

    public GuildSetPvPPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getGuildTag() {
        return guildTag;
    }

    public String getExecutorName() {
        return executorName;
    }
}
