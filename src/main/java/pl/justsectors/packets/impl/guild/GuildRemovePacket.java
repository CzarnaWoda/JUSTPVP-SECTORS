package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class GuildRemovePacket extends RedisPacket {

    private String guildTag;
    private String executorName;


    public GuildRemovePacket(String guildTag, String executorName) {
        this.guildTag = guildTag;
        this.executorName = executorName;
    }

    public GuildRemovePacket() {
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
