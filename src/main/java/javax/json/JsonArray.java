package javax.json;

import java.util.List;

public interface JsonArray extends JsonStructure, List<JsonValue>
{
    JsonObject getJsonObject(int p0);
    
    JsonArray getJsonArray(int p0);
    
    JsonNumber getJsonNumber(int p0);
    
    JsonString getJsonString(int p0);
    
     <T extends JsonValue> List<T> getValuesAs(Class<T> p0);
    
    String getString(int p0);
    
    String getString(int p0, String p1);
    
    int getInt(int p0);
    
    int getInt(int p0, int p1);
    
    boolean getBoolean(int p0);
    
    boolean getBoolean(int p0, boolean p1);
    
    boolean isNull(int p0);
}
