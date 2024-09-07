package javax.json;

import java.util.Map;

public interface JsonBuilderFactory
{
    JsonObjectBuilder createObjectBuilder();
    
    JsonArrayBuilder createArrayBuilder();
    
    Map<String, ?> getConfigInUse();
}
