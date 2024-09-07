package javax.json.stream;

import java.io.Closeable;
import java.math.BigDecimal;

public interface JsonParser extends Closeable
{
    boolean hasNext();
    
    Event next();
    
    String getString();
    
    boolean isIntegralNumber();
    
    int getInt();
    
    long getLong();
    
    BigDecimal getBigDecimal();
    
    JsonLocation getLocation();
    
    void close();
    
    public enum Event
    {
        START_ARRAY, 
        START_OBJECT, 
        KEY_NAME, 
        VALUE_STRING, 
        VALUE_NUMBER, 
        VALUE_TRUE, 
        VALUE_FALSE, 
        VALUE_NULL, 
        END_OBJECT, 
        END_ARRAY;
    }
}
