package javax.json.stream;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

public interface JsonGeneratorFactory
{
    JsonGenerator createGenerator(Writer p0);
    
    JsonGenerator createGenerator(OutputStream p0);
    
    JsonGenerator createGenerator(OutputStream p0, Charset p1);
    
    Map<String, ?> getConfigInUse();
}
