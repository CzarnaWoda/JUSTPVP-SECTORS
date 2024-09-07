package pl.justsectors.packets.impl.user;

import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class ClearInventoryPacket extends RedisPacket {

    private String playerName;
    private String executorName;

    public ClearInventoryPacket(){}
    public ClearInventoryPacket(String playerName, String executorName){
        this.playerName = playerName;
        this.executorName = executorName;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getExecutorName() {
        return executorName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
