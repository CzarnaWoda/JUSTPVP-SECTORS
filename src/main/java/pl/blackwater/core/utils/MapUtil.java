package pl.blackwater.core.utils;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MapUtil {

    public static String convertUUIDLONGMapToString(HashMap<UUID, Long> times){
        AtomicReference<String> r = new AtomicReference<>("");
        times.forEach((uuid, aLong) -> r.set(r + uuid.toString() + "=" + aLong + ";"));
        return r.get();
    }
    public static HashMap<UUID, Long> convertStringToMapUUIDLONG(String string){
        HashMap<UUID, Long> times = new HashMap<>();
        if(string.equals("")){
            return times;
        }
        String[] array = string.split(";");
        for(String s : array){
            String[] values = s.split("=");
            final UUID uuid = UUID.fromString(values[0]);
            final long time = Long.parseLong(values[1]);
            times.put(uuid,time);
        }
        return times;
    }
}
