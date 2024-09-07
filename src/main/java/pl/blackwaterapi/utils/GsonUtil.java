package pl.blackwaterapi.utils;

import com.google.gson.*;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import pl.blackwaterapi.utils.adapters.ItemStackAdapter;
import pl.blackwaterapi.utils.adapters.LocationAdapter;

import java.lang.reflect.Type;

public class GsonUtil {

    private static final GsonBuilder gsonBuilder;
    private static final Gson gson;

    static {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Location.class, new LocationAdapter());
        gson = gsonBuilder.create();
    }

    public static JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }

    public static JsonElement toJsonTree(Object src, Type typeOfSrc) {
        return gson.toJsonTree(src, typeOfSrc);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Class<T> classOfT){
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

}
