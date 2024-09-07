package javax.json.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.JsonValue;

public interface JsonGenerator extends Flushable, Closeable
{
    public static final String PRETTY_PRINTING = "javax.json.stream.JsonGenerator.prettyPrinting";
    
    JsonGenerator writeStartObject();
    
    JsonGenerator writeStartObject(String p0);
    
    JsonGenerator writeStartArray();
    
    JsonGenerator writeStartArray(String p0);
    
    JsonGenerator write(String p0, JsonValue p1);
    
    JsonGenerator write(String p0, String p1);
    
    JsonGenerator write(String p0, BigInteger p1);
    
    JsonGenerator write(String p0, BigDecimal p1);
    
    JsonGenerator write(String p0, int p1);
    
    JsonGenerator write(String p0, long p1);
    
    JsonGenerator write(String p0, double p1);
    
    JsonGenerator write(String p0, boolean p1);
    
    JsonGenerator writeNull(String p0);
    
    JsonGenerator writeEnd();
    
    JsonGenerator write(JsonValue p0);
    
    JsonGenerator write(String p0);
    
    JsonGenerator write(BigDecimal p0);
    
    JsonGenerator write(BigInteger p0);
    
    JsonGenerator write(int p0);
    
    JsonGenerator write(long p0);
    
    JsonGenerator write(double p0);
    
    JsonGenerator write(boolean p0);
    
    JsonGenerator writeNull();
    
    void close();
    
    void flush();
}
