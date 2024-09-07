package javax.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface JsonArrayBuilder
{
    JsonArrayBuilder add(JsonValue p0);
    
    JsonArrayBuilder add(String p0);
    
    JsonArrayBuilder add(BigDecimal p0);
    
    JsonArrayBuilder add(BigInteger p0);
    
    JsonArrayBuilder add(int p0);
    
    JsonArrayBuilder add(long p0);
    
    JsonArrayBuilder add(double p0);
    
    JsonArrayBuilder add(boolean p0);
    
    JsonArrayBuilder addNull();
    
    JsonArrayBuilder add(JsonObjectBuilder p0);
    
    JsonArrayBuilder add(JsonArrayBuilder p0);
    
    JsonArray build();
}
