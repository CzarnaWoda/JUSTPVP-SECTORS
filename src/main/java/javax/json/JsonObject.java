package javax.json;

import java.util.Map;

public interface JsonObject extends JsonStructure, Map<String, JsonValue>
{
    JsonArray getJsonArray(String p0);
    
    JsonObject getJsonObject(String p0);
    
    JsonNumber getJsonNumber(String p0);
    
    JsonString getJsonString(String p0);
    
    String getString(String p0);
    
    String getString(String p0, String p1);
    
    int getInt(String p0);
    
    int getInt(String p0, int p1);
    
    boolean getBoolean(String p0);
    
    boolean getBoolean(String p0, boolean p1);
    
    boolean isNull(String p0);
}
