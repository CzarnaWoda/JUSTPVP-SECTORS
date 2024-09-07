package pl.blackwater.core.tablist;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Reflections
{
    private static final Map<String, Class<?>> classCache;
    private static final Map<String, Field> fieldCache;
    private static final Map<String, FieldAccessor<?>> fieldAccessorCache;
    private static final Map<String, Method> methodCache;
    private static final Class<?> INVALID_CLASS;
    private static final Method INVALID_METHOD;
    private static final Field INVALID_FIELD;
    private static final FieldAccessor<?> INVALID_FIELD_ACCESSOR;

    public static String getVersion() {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf(46) + 1) + ".";
    }

    public static String getFixedVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public static Class<?> getClassOmitCache(final String className) {
        Reflections.classCache.remove(className);
        return getClass(className);
    }

    public static Class<?> getClass(final String className) {
        Class<?> c = Reflections.classCache.get(className);
        if (c != null) {
            return (c != Reflections.INVALID_CLASS) ? c : null;
        }
        try {
            c = Class.forName(className);
            Reflections.classCache.put(className, c);
        }
        catch (Exception e) {
            if (FunnyLogger.exception(e.getCause())) {
                e.printStackTrace();
            }
            Reflections.classCache.put(className, Reflections.INVALID_CLASS);
        }
        return c;
    }

    public static Class<?> getNMSClass(final String name) {
        return getClass("net.minecraft.server." + getVersion() + name);
    }

    public static Class<?> getCraftBukkitClass(final String name) {
        return getClass("org.bukkit.craftbukkit." + getVersion() + name);
    }

    public static Class<?> getBukkitClass(final String name) {
        return getClass("org.bukkit." + name);
    }

    public static Object getHandle(final Entity entity) {
        try {
            return getMethod(entity.getClass(), "getHandle").invoke(entity, new Object[0]);
        }
        catch (Exception e) {
            if (FunnyLogger.exception(e.getCause())) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static Object getHandle(final World world) {
        try {
            return getMethod(world.getClass(), "getHandle").invoke(world, new Object[0]);
        }
        catch (Exception e) {
            if (FunnyLogger.exception(e.getCause())) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static String constructFieldCacheKey(final Class<?> cl, final String fieldName) {
        return cl.getName() + "." + fieldName;
    }

    public static Field getField(final Class<?> cl, final String fieldName) {
        final String cacheKey = constructFieldCacheKey(cl, fieldName);
        Field field = Reflections.fieldCache.get(cacheKey);
        if (field != null) {
            return (field != Reflections.INVALID_FIELD) ? field : null;
        }
        try {
            field = cl.getDeclaredField(fieldName);
            Reflections.fieldCache.put(cacheKey, field);
        }
        catch (Exception e) {
            if (FunnyLogger.exception(e.getCause())) {
                e.printStackTrace();
            }
            Reflections.fieldCache.put(cacheKey, Reflections.INVALID_FIELD);
        }
        return field;
    }

    public static <T> FieldAccessor<T> getField(final Class<?> target, final Class<T> fieldType, final int index) {
        return getField(target, null, fieldType, index);
    }

    private static <T> FieldAccessor<T> getField(final Class<?> target, final String name, final Class<T> fieldType, int index) {
        final String cacheKey = target.getName() + "." + ((name != null) ? name : "NONE") + "." + fieldType.getName() + "." + index;
        FieldAccessor<T> output = (FieldAccessor<T>)Reflections.fieldAccessorCache.get(cacheKey);
        if (output != null) {
            if (output == Reflections.INVALID_FIELD_ACCESSOR) {
                throw new IllegalArgumentException("Cannot find field with type " + fieldType);
            }
            return output;
        }
        else {
            for (final Field field : target.getDeclaredFields()) {
                if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                    field.setAccessible(true);
                    output = new FieldAccessor<T>() {
                        @Override
                        public T get(final Object target) {
                            try {
                                return (T)field.get(target);
                            }
                            catch (IllegalAccessException e) {
                                throw new RuntimeException("Cannot access reflection.", e);
                            }
                        }

                        @Override
                        public void set(final Object target, final Object value) {
                            try {
                                field.set(target, value);
                            }
                            catch (IllegalAccessException e) {
                                throw new RuntimeException("Cannot access reflection.", e);
                            }
                        }

                        @Override
                        public boolean hasField(final Object target) {
                            return field.getDeclaringClass().isAssignableFrom(target.getClass());
                        }
                    };
                    break;
                }
            }
            if (output == null && target.getSuperclass() != null) {
                output = (FieldAccessor<T>)getField(target.getSuperclass(), name, (Class<Object>)fieldType, index);
            }
            Reflections.fieldAccessorCache.put(cacheKey, (output != null) ? output : Reflections.INVALID_FIELD_ACCESSOR);
            if (output == null) {
                throw new IllegalArgumentException("Cannot find field with type " + fieldType);
            }
            return output;
        }
    }

    public static Field getPrivateField(final Class<?> cl, final String fieldName) {
        final String cacheKey = constructFieldCacheKey(cl, fieldName);
        Field c = Reflections.fieldCache.get(cacheKey);
        if (c != null) {
            return (c != Reflections.INVALID_FIELD) ? c : null;
        }
        try {
            c = cl.getDeclaredField(fieldName);
            c.setAccessible(true);
            Reflections.fieldCache.put(cacheKey, c);
        }
        catch (Exception e) {
            if (FunnyLogger.exception(e.getCause())) {
                e.printStackTrace();
            }
            Reflections.fieldCache.put(cacheKey, Reflections.INVALID_FIELD);
        }
        return c;
    }

    public static Method getMethod(final Class<?> cl, final String method, final Class<?>... args) {
        final String cacheKey = cl.getName() + "." + method + "." + ((args == null) ? "NONE" : Arrays.toString(args));
        Method output = Reflections.methodCache.get(cacheKey);
        if (output != null) {
            return (output != Reflections.INVALID_METHOD) ? output : null;
        }
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(method) && (args == null || classListEqual(args, m.getParameterTypes()))) {
                output = m;
                break;
            }
        }
        Reflections.methodCache.put(cacheKey, (output == null) ? Reflections.INVALID_METHOD : output);
        return output;
    }

    public static Method getMethod(final Class<?> cl, final String method) {
        return getMethod(cl, method, (Class<?>[])null);
    }

    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... arguments) {
        for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (Arrays.equals(constructor.getParameterTypes(), arguments)) {
                return constructor;
            }
        }
        return null;
    }

    public static boolean classListEqual(final Class<?>[] l1, final Class<?>[] l2) {
        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; ++i) {
            if (l1[i] != l2[i]) {
                return false;
            }
        }
        return true;
    }

    static {
        classCache = new HashMap<String, Class<?>>();
        fieldCache = new HashMap<String, Field>();
        fieldAccessorCache = new HashMap<String, FieldAccessor<?>>();
        methodCache = new HashMap<String, Method>();
        INVALID_CLASS = InvalidMarker.class;
        INVALID_METHOD = SafeUtils.safeInit(() -> InvalidMarker.class.getDeclaredMethod("invalidMethodMaker", (Class<?>[])new Class[0]));
        INVALID_FIELD = SafeUtils.safeInit(() -> InvalidMarker.class.getDeclaredField("invalidFieldMarker"));
        INVALID_FIELD_ACCESSOR = getField(Reflections.INVALID_CLASS, (Class<?>)Void.class, 0);
    }

    private static class InvalidMarker
    {
        public Void invalidFieldMarker;

        public void invalidMethodMaker() {
        }
    }

    public interface FieldAccessor<T>
    {
        T get(final Object p0);

        void set(final Object p0, final Object p1);

        boolean hasField(final Object p0);
    }

    public interface MethodInvoker
    {
        Object invoke(final Object p0, final Object... p1);
    }

    public interface ConstructorInvoker
    {
        Object invoke(final Object... p0);
    }
}
