package javax.json;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

public interface JsonWriterFactory
{
    JsonWriter createWriter(Writer p0);
    
    JsonWriter createWriter(OutputStream p0);
    
    JsonWriter createWriter(OutputStream p0, Charset p1);
    
    Map<String, ?> getConfigInUse();
}
