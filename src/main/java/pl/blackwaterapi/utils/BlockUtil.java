package pl.blackwaterapi.utils;

public class BlockUtil
{
    private static Reflection.FieldAccessor<Float> durabilityField;
    private static Reflection.FieldAccessor<Float> strengthField;
    
    public static void setDurability(String name, float durability) {
        Reflection.FieldAccessor<Object> f = Reflection.getSimpleField(Reflection.getMinecraftClass("Blocks"), name.toUpperCase());
        BlockUtil.durabilityField.set(f.get(null), durability);
        Logger.info("Durability of " + name + " was changed to " + durability);
    }
    
    public static void setStrength(String name, float strength) {
        Reflection.FieldAccessor<Object> f = Reflection.getSimpleField(Reflection.getMinecraftClass("Blocks"), name.toUpperCase());
        BlockUtil.strengthField.set(f.get(null), strength);
        Logger.info("Strength of " + name + " was changed to " + strength);
    }
    
    static {
        BlockUtil.durabilityField = Reflection.getField(Reflection.getMinecraftClass("Block"), "durability", Float.TYPE);
        BlockUtil.strengthField = Reflection.getField(Reflection.getMinecraftClass("Block"), "strength", Float.TYPE);
    }
}
