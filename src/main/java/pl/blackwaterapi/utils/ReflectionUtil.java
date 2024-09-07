package pl.blackwaterapi.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.Packet;


public class ReflectionUtil
{
    private static String version;
    
    public static Class<?> getOBCClass(String name) {
        Class<?> c = null;
        try {
            c = Class.forName("org.bukkit.craftbukkit." + ReflectionUtil.version + "." + name);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    
    public static Class<?> getCraftClass(String name) {
        String className = "net.minecraft.server." + getVersion() + name;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    
    public static Class<?> getBukkitClass(String name) {
        String className = "org.bukkit.craftbukkit." + getVersion() + name;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    
    public static Class<?> getNMSClass(String name) {
        Class<?> c = null;
        try {
            c = Class.forName("net.minecraft.server." + ReflectionUtil.version + "." + name);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    
    public static void setValue(Field f, Object o, Object v) {
        try {
            f.setAccessible(true);
            f.set(o, v);
            f.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Object getHandle(Entity entity) {
        try {
            return Objects.requireNonNull(getMethod(entity.getClass(), "getHandle")).invoke(entity);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getHandle(World world) {
        try {
            return Objects.requireNonNull(getMethod(world.getClass(), "getHandle")).invoke(world);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Field getField(Class<?> cl, String field_name) {
        try {
            return cl.getDeclaredField(field_name);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> FieldAccessor<T> getField(Class<?> target, Class<T> fieldType, int index) {
        return getField(target, null, fieldType, index);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) {
        for (Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return new FieldAccessor<T>() {
					@Override
                    public T get(Object target) {
                        try {
                            return (T)field.get(target);
                        }
                        catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }
                    
                    @Override
                    public void set(Object target, Object value) {
                        try {
                            field.set(target, value);
                        }
                        catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }
                    
                    @Override
                    public boolean hasField(Object target) {
                        return field.getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
        }
        if (target.getSuperclass() != null) {
            return (FieldAccessor<T>)getField(target.getSuperclass(), name, (Class<Object>)fieldType, index);
        }
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }
    
    public static Field getPrivateField(Class<?> cl, String field_name) {
        try {
            Field field = cl.getDeclaredField(field_name);
            field.setAccessible(true);
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setValue(Object instance, String fieldName, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
    
    public static Object getValue(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
    
    public static Method getMethod(Class<?> cl, String method, Class<?>... args) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method) && classListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }
    
    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
    
    private static boolean classListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; ++i) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }
    
    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf(46) + 1) + ".";
    }
    
    @SuppressWarnings("rawtypes")
	public static void sendPacket(Player p, Packet packet) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }
    
    @SuppressWarnings("rawtypes")
	public static void sendPacket(Packet packet) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendPacket(p, packet);
        }
    }
    
    @SuppressWarnings("rawtypes")
	public static void sendPacket(Packet packet, Player[] except) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i > except.length; ++i) {
                if (!except[i].equals(p)) {
                    sendPacket(p, packet);
                }
            }
        }
    }
    
    static {
        version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }
    
    public interface FieldAccessor<T>
    {
        T get(Object p0);
        
        void set(Object p0, Object p1);
        
        boolean hasField(Object p0);
    }
    
    public interface MethodInvoker
    {
        Object invoke(Object p0, Object... p1);
    }
    
    public interface ConstructorInvoker
    {
        Object invoke(Object... p0);
    }
}
