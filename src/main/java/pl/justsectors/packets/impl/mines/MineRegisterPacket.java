package pl.justsectors.packets.impl.mines;

import pl.blackwater.core.data.Mine;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;

public class MineRegisterPacket extends RedisPacket {

    final Mine mine;

    public MineRegisterPacket(Mine mine) {
        this.mine = mine;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public Mine getMine() {
        return mine;
    }
}
