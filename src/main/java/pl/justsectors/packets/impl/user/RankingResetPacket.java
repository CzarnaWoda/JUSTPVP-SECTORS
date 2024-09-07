package pl.justsectors.packets.impl.user;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class RankingResetPacket extends RedisPacket {

    private String executorName;
    private String receiverName;

    public RankingResetPacket(String executorName, String receiverName) {
        this.executorName = executorName;
        this.receiverName = receiverName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getExecutorName() {
        return executorName;
    }

    public String getReceiverName() {
        return receiverName;
    }
}
