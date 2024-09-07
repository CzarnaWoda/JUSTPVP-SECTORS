package pl.blackwater.core.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PacketSender
{
    private static Method getHandle;
    private static Field playerConnection;
    private static Method sendPacket;

    public static void sendPacket(final Collection<? extends Player> players, final Object... packets) {
        final List<Object> packetList = Arrays.asList(packets);
        players.forEach(p -> sendPacket(p, packetList));
    }

    public static void sendPacket(final Player[] players, final Object... packets) {
        final List<Object> packetList = Arrays.asList(packets);
        for (final Player player : players) {
            sendPacket(player, packetList);
        }
    }

    public static void sendPacket(final Object... packets) {
        sendPacket(Arrays.asList(packets));
    }

    public static void sendPacket(final Player target, final Object... packets) {
        sendPacket(target, Arrays.asList(packets));
    }

    public static void sendPacket(final List<Object> packets) {
        Bukkit.getOnlinePlayers().forEach(p -> sendPacket(p, packets));
    }

    public static void sendPacket(final Player target, final List<Object> packets) {
        if (target == null) {
            return;
        }
        try {
            final Object handle = PacketSender.getHandle.invoke(target);
            final Object connection = PacketSender.playerConnection.get(handle);
            for (final Object packet : packets) {
                PacketSender.sendPacket.invoke(connection, packet);
            }
        }
        catch (IllegalAccessException | InvocationTargetException ex3) {
            ex3.printStackTrace();
        }
    }

    static {
        try {
            PacketSender.getHandle = Reflections.getMethod(Reflections.getCraftBukkitClass("entity.CraftPlayer"), "getHandle");
            PacketSender.sendPacket = Reflections.getMethod(Reflections.getNMSClass("PlayerConnection"), "sendPacket");
            PacketSender.playerConnection = Reflections.getField(Reflections.getNMSClass("EntityPlayer"), "playerConnection");
        }
        catch (Exception e) {
            if (FunnyLogger.exception(e.getCause())) {
                e.printStackTrace();
            }
        }
    }
}

