package javax.json;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

public interface JsonReaderFactory
{
    JsonReader createReader(Reader p0);
    
    JsonReader createReader(InputStream p0);
    
    JsonReader createReader(InputStream p0, Charset p1);
    
    Map<String, ?> getConfigInUse();
}
