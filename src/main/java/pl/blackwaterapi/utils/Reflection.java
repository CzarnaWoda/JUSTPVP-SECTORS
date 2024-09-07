package pl.blackwaterapi.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public class Reflection
{
    private static String OBC_PREFIX;
    private static String NMS_PREFIX;
    private static String VERSION;
    private static Pattern MATCH_VARIABLE;

	public static <T> FieldAccessor<T> getSimpleField(Class<?> target, String name) {
        return (FieldAccessor<T>)getField(target, name);
    }
    
    public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType) {
        return getField(target, name, fieldType, 0);
    }
    
    public static <T> FieldAccessor<T> getField(String className, String name, Class<T> fieldType) {
        return getField(getClass(className), name, fieldType, 0);
    }
    
    public static <T> FieldAccessor<T> getField(Class<?> target, Class<T> fieldType, int index) {
        return getField(target, null, fieldType, index);
    }
    
    public static <T> FieldAccessor<T> getField(String className, Class<T> fieldType, int index) {
        return getField(getClass(className), fieldType, index);
    }
    
    @SuppressWarnings("unchecked")
	private static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) {
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
    
    @SuppressWarnings("unchecked")
	private static <T> FieldAccessor<T> getField(Class<?> target, String name) {
        for (Field field : target.getDeclaredFields()) {
            if (name == null || field.getName().equals(name)) {
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
            return (FieldAccessor<T>)getField(target.getSuperclass(), name);
        }
        throw new IllegalArgumentException("Cannot find field with type");
    }
    
    public static MethodInvoker getMethod(String className, String methodName, Class<?>... params) {
        return getTypedMethod(getClass(className), methodName, null, params);
    }
    
    public static MethodInvoker getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        return getTypedMethod(clazz, methodName, null, params);
    }
    
    public static MethodInvoker getTypedMethod(Class<?> clazz, String methodName, Class<?> returnType, Class<?>... params) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (((methodName == null || method.getName().equals(methodName)) && returnType == null) || (method.getReturnType().equals(returnType) && Arrays.equals(method.getParameterTypes(), params))) {
                method.setAccessible(true);
                return (target, arguments) -> {
                    try {
                        return method.invoke(target, arguments);
                    }
                    catch (Exception e) {
                        throw new RuntimeException("Cannot invoke method " + method, e);
                    }
                };
            }
        }
        if (clazz.getSuperclass() != null) {
            return getMethod(clazz.getSuperclass(), methodName, params);
        }
        throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, Arrays.asList(params)));
    }
    
    public static ConstructorInvoker getConstructor(String className, Class<?>... params) {
        return getConstructor(getClass(className), params);
    }
    
    public static ConstructorInvoker getConstructor(Class<?> clazz, Class<?>... params) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (Arrays.equals(constructor.getParameterTypes(), params)) {
                constructor.setAccessible(true);
                return arguments -> {
                    try {
                        return constructor.newInstance(arguments);
                    }
                    catch (Exception e) {
                        throw new RuntimeException("Cannot invoke constructor " + constructor, e);
                    }
                };
            }
        }
        throw new IllegalStateException(String.format("Unable to find constructor for %s (%s).", clazz, Arrays.asList(params)));
    }
    
    @SuppressWarnings("unchecked")
	public static Class<Object> getUntypedClass(String lookupName) {
        return (Class<Object>)getClass(lookupName);
    }
    
    public static Class<?> getClass(String lookupName) {
        return getCanonicalClass(expandVariables(lookupName));
    }
    
    public static Class<?> getMinecraftClass(String name) {
        return getCanonicalClass(Reflection.NMS_PREFIX + "." + name);
    }
    
    public static Class<?> getCraftBukkitClass(String name) {
        return getCanonicalClass(Reflection.OBC_PREFIX + "." + name);
    }
    
    private static Class<?> getCanonicalClass(String canonicalName) {
        try {
            return Class.forName(canonicalName);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot find " + canonicalName, e);
        }
    }
    
    private static String expandVariables(String name) {
        StringBuffer output = new StringBuffer();
        Matcher matcher = Reflection.MATCH_VARIABLE.matcher(name);
        while (matcher.find()) {
            String variable = matcher.group(1);
            String replacement;
            if ("nms".equalsIgnoreCase(variable)) {
                replacement = Reflection.NMS_PREFIX;
            }
            else if ("obc".equalsIgnoreCase(variable)) {
                replacement = Reflection.OBC_PREFIX;
            }
            else {
                if (!"version".equalsIgnoreCase(variable)) {
                    throw new IllegalArgumentException("Unknown variable: " + variable);
                }
                replacement = Reflection.VERSION;
            }
            if (replacement.length() > 0 && matcher.end() < name.length() && name.charAt(matcher.end()) != '.') {
                replacement += ".";
            }
            matcher.appendReplacement(output, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(output);
        return output.toString();
    }
    
    static {
        Reflection.OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
        Reflection.NMS_PREFIX = Reflection.OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");
        Reflection.VERSION = Reflection.OBC_PREFIX.replace("org.bukkit.craftbukkit", "").replace(".", "");
        Reflection.MATCH_VARIABLE = Pattern.compile("\\{([^\\}]+)\\}");
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
