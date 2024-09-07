package javax.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface JsonNumber extends JsonValue
{
    boolean isIntegral();
    
    int intValue();
    
    int intValueExact();
    
    long longValue();
    
    long longValueExact();
    
    BigInteger bigIntegerValue();
    
    BigInteger bigIntegerValueExact();
    
    double doubleValue();
    
    BigDecimal bigDecimalValue();
    
    String toString();
    
    boolean equals(Object p0);
    
    int hashCode();
}
