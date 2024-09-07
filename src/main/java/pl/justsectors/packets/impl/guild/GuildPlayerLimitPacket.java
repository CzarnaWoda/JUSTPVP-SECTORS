package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class GuildPlayerLimitPacket extends RedisPacket {

    private String guildTag;
    private String executorName;
    private int value;

    public GuildPlayerLimitPacket(String guildTag, String executorName, int value) {
        this.guildTag = guildTag;
        this.executorName = executorName;
        this.value = value;
    }

    public GuildPlayerLimitPacket() {
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }


    public String getExecutorName() {
        return executorName;
    }

    public String getGuildTag() {
        return guildTag;
    }

    public int getLimit() {
        return value;
    }
}
