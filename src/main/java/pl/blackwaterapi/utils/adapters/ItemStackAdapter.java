package pl.blackwaterapi.utils.adapters;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.blackwaterapi.utils.GsonUtil;


public class ItemStackAdapter implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    private static final String MATERIAL = "material";
    private static final String AMOUNT = "amount";
    private static final String DURABILITY = "durability";
    private static final String DISPLAY_NAME = "displayName";
    private static final String LORE = "lore";
    private static final String ENCHANTMENTS = "enchants";


    @SuppressWarnings("unchecked")
    @Override
    public ItemStack deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        try {
            final JsonObject obj = json.getAsJsonObject();
            final Material material = Material.getMaterial(obj.get(ItemStackAdapter.MATERIAL).getAsInt());
            final ItemStack itemStack = new ItemStack(material, obj.get(ItemStackAdapter.AMOUNT).getAsInt(), (short) obj.get(ItemStackAdapter.DURABILITY).getAsInt());
            final JsonElement displayName = obj.get(ItemStackAdapter.DISPLAY_NAME);
            final JsonElement lore = obj.get(ItemStackAdapter.LORE);
            if (displayName != null || lore != null) {
                final ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);
                if (displayName != null) {
                    meta.setDisplayName(displayName.getAsString());
                }
                if (lore != null) {
                    meta.setLore(GsonUtil.fromJson(lore, List.class));
                }
                itemStack.setItemMeta(meta);
            }
            final JsonElement enchants = obj.get(ItemStackAdapter.ENCHANTMENTS);
            if (enchants != null) {
                final Map<String, Double> enchantsMap = GsonUtil.fromJson(enchants, Map.class);
                for (final Entry<String, Double> entry : enchantsMap.entrySet()) {
                    itemStack.addEnchantment(Enchantment.getByName(entry.getKey()), entry.getValue().intValue());
                }
            }
            return itemStack;
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public JsonElement serialize(final ItemStack src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject obj = new JsonObject();
        try {
            final ItemMeta meta = src.getItemMeta();
            obj.addProperty(ItemStackAdapter.MATERIAL, src.getType().getId());
            obj.addProperty(ItemStackAdapter.AMOUNT, src.getAmount());
            obj.addProperty(ItemStackAdapter.DURABILITY, src.getDurability());
            obj.addProperty(ItemStackAdapter.DISPLAY_NAME, meta.getDisplayName());
            obj.add(ItemStackAdapter.LORE, GsonUtil.toJsonTree(meta.getLore(), List.class));
            final Map<String, Integer> enchants = new HashMap<>();
            for (final Entry<Enchantment, Integer> entry : src.getEnchantments().entrySet()) {
                enchants.put(entry.getKey().getName(), entry.getValue());
            }
            obj.add(ItemStackAdapter.ENCHANTMENTS, GsonUtil.toJsonTree(enchants, Map.class));
            return obj;
        } catch (final Exception ex) {
            ex.printStackTrace();
            return obj;
        }
    }
}