package javax.json.stream;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

public interface JsonParserFactory
{
    JsonParser createParser(Reader p0);
    
    JsonParser createParser(InputStream p0);
    
    JsonParser createParser(InputStream p0, Charset p1);
    
    JsonParser createParser(JsonObject p0);
    
    JsonParser createParser(JsonArray p0);
    
    Map<String, ?> getConfigInUse();
}
