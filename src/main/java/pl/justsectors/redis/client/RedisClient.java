package pl.justsectors.redis.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.blackwaterapi.utils.GsonUtil;
import pl.justsectors.packets.RedisPacket;
import pl.justsectors.packets.manager.PacketManager;
import pl.justsectors.redis.RedisManager;
import pl.justsectors.redis.enums.ChannelType;
import pl.justsectors.sectors.Sector;
import pl.justsectors.sectors.SectorManager;

public class RedisClient {


    public static void sendGlobalPacket(final RedisPacket packet)
    {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.GLOBAL_PACKETS).sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }


    public static void sendSectorsPacket(final RedisPacket packet)
    {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_SPIGOTS).sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }

    public static void sendPacket(final RedisPacket packet, final String sectorName)
    {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_SINGLE_SECTOR).sendMessage(sectorName + "#" + packetID + "@" + GsonUtil.toJson(packet));
    }

    public static void sendProxiesPacket(final RedisPacket packet)
    {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_PROXIES).sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }

    public static void sendPacket(final RedisPacket packet, final Sector sector)
    {
        sendPacket(packet, sector.getSectorName());
    }

    public static void sendUser(RedisPacket packet, String sectorName)
    {
        RedisManager.getRedisListener(ChannelType.USER).sendMessage(sectorName, packet);
    }

    public static void sendUser(RedisPacket packet)
    {
        for(Sector sector : SectorManager.getSectors().values())
        {
            sendUser(packet, sector.getSectorName());
        }
    }

}
