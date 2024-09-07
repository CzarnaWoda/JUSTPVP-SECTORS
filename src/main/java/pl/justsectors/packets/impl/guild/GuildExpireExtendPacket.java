package pl.justsectors.packets.impl.guild;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class GuildExpireExtendPacket extends RedisPacket {

    private String guildTag;
    private String executorName;
    private Long expireValue;

    public GuildExpireExtendPacket(String guildTag, String executorName, Long expireValue) {
        this.guildTag = guildTag;
        this.executorName = executorName;
        this.expireValue = expireValue;
    }

    public GuildExpireExtendPacket() {
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

    public Long getExpireValue() {
        return expireValue;
    }
}
