package javax.json;

public interface JsonString extends JsonValue
{
    String getString();
    
    CharSequence getChars();
    
    boolean equals(Object p0);
    
    int hashCode();
}
