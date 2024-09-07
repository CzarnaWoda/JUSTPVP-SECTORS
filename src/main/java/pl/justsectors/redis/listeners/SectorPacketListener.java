package pl.justsectors.redis.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.handler.PacketHandler;
import pl.justsectors.packets.handler.PacketHandlerImpl;
import pl.justsectors.packets.manager.PacketManager;
import pl.justsectors.redis.RedisManager;
import pl.justsectors.redis.enums.ChannelType;
import pl.justsectors.redis.listeners.api.RedisListener;

public class SectorPacketListener<T extends String> extends RedisListener<T> {

    final PacketHandler packetHandler = new PacketHandlerImpl();

    public SectorPacketListener(ChannelType type, RTopic rTopic) {
        super(type, rTopic);
        getTopic().addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence charSequence, String packet) {
                final String[] split = packet.split("@");
                Class<? extends RedisPacket> clzPacket = PacketManager.getPacketClass(Integer.parseInt(split[0]));
                if (clzPacket == null) return;
                final RedisPacket p = GsonUtil.fromJson(split[1], clzPacket);
                p.handlePacket(packetHandler);
            }
        });
    }


    @Override
    public void sendMessage(T message) {
        final String[] split = message.split("#");
        RedisManager.getRedisConnection().getTopic(split[0])
                .publish(split[1]);
    }
}