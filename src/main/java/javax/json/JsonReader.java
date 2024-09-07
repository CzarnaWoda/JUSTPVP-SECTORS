package javax.json;

import java.io.Closeable;

public interface JsonReader extends Closeable
{
    JsonStructure read();
    
    JsonObject readObject();
    
    JsonArray readArray();
    
    void close();
}
