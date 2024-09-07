package javax.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface JsonObjectBuilder
{
    JsonObjectBuilder add(String p0, JsonValue p1);
    
    JsonObjectBuilder add(String p0, String p1);
    
    JsonObjectBuilder add(String p0, BigInteger p1);
    
    JsonObjectBuilder add(String p0, BigDecimal p1);
    
    JsonObjectBuilder add(String p0, int p1);
    
    JsonObjectBuilder add(String p0, long p1);
    
    JsonObjectBuilder add(String p0, double p1);
    
    JsonObjectBuilder add(String p0, boolean p1);
    
    JsonObjectBuilder addNull(String p0);
    
    JsonObjectBuilder add(String p0, JsonObjectBuilder p1);
    
    JsonObjectBuilder add(String p0, JsonArrayBuilder p1);
    
    JsonObject build();
}
