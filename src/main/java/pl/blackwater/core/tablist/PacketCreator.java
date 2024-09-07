package pl.blackwater.core.tablist;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PacketCreator
{
    private static final Map<String, ThreadLocal<PacketCreator>> PACKET_CREATOR_CACHE;
    private final Class<?> packetClass;
    private final Map<String, Field> packetFields;
    private Object packetInstance;

    private PacketCreator(final Class<?> packetClass) {
        this.packetClass = packetClass;
        this.packetFields = new HashMap<String, Field>(this.packetClass.getDeclaredFields().length);
        for (final Field field : this.packetClass.getDeclaredFields()) {
            field.setAccessible(true);
            this.packetFields.put(field.getName(), field);
        }
    }

    public static PacketCreator of(final String packetClassName) {
        ThreadLocal<PacketCreator> creator = PacketCreator.PACKET_CREATOR_CACHE.get(packetClassName);
        if (creator == null) {
            final Class<?> packetClass = Reflections.getNMSClass(packetClassName);
            creator = ThreadLocal.withInitial(() -> new PacketCreator(packetClass));
            PacketCreator.PACKET_CREATOR_CACHE.put(packetClassName, creator);
        }
        return creator.get();
    }

    public static PacketCreator of(final Class<?> packetClass) {
        ThreadLocal<PacketCreator> creator = PacketCreator.PACKET_CREATOR_CACHE.get(packetClass.getName());
        if (creator == null) {
            creator = ThreadLocal.withInitial(() -> new PacketCreator(packetClass));
            PacketCreator.PACKET_CREATOR_CACHE.put(packetClass.getName(), creator);
        }
        return creator.get();
    }

    public PacketCreator create() {
        try {
            this.packetInstance = this.packetClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex = ex2;
            final ReflectiveOperationException e = ex;
            e.printStackTrace();
        }
        return this;
    }

    public PacketCreator withField(final String fieldName, final Object value) {
        Validate.notNull(value, "Value cannot be NULL!");
        if (this.packetInstance == null) {
            throw new RuntimeException("Tried to set field on non-existing packet instance!");
        }
        try {
            final Field field = this.packetFields.get(fieldName);
            field.set(this.packetInstance, value);
        }
        catch (IllegalAccessException ex) {
            FunnyLogger.exception(ex.getMessage(), ex.getStackTrace());
        }
        return this;
    }

    public PacketCreator withField(final String fieldName, final Object value, final Class<?> fieldType) {
        Validate.notNull(value, "Value cannot be NULL!");
        if (this.packetInstance == null) {
            throw new RuntimeException("Tried to set field on non-existing packet instance!");
        }
        try {
            final Field field = this.packetFields.get(fieldName);
            if (!fieldType.isAssignableFrom(field.getType())) {
                FunnyLogger.error("Given fieldType is not assignable from found field's type");
            }
            field.set(this.packetInstance, value);
        }
        catch (IllegalAccessException ex) {
            FunnyLogger.exception(ex.getMessage(), ex.getStackTrace());
        }
        return this;
    }

    public void send(final Collection<? extends Player> players) {
        PacketSender.sendPacket(players, this.getPacket());
    }

    public Object getPacket() {
        return this.packetInstance;
    }

    static {
        PACKET_CREATOR_CACHE = new HashMap<String, ThreadLocal<PacketCreator>>();
    }
}

