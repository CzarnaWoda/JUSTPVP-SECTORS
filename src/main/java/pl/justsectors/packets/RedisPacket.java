package pl.justsectors.packets;

import pl.justsectors.packets.handler.PacketHandler;

public abstract class RedisPacket {

    public abstract void handlePacket(final PacketHandler handler);



}
