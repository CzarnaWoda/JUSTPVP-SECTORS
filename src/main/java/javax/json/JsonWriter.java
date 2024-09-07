package javax.json;

import java.io.Closeable;

public interface JsonWriter extends Closeable
{
    void writeArray(JsonArray p0);
    
    void writeObject(JsonObject p0);
    
    void write(JsonStructure p0);
    
    void close();
}
