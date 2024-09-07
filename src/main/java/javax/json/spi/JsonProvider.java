package javax.json.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonException;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

public abstract class JsonProvider
{
    @SuppressWarnings("unused")
	private static final String DEFAULT_PROVIDER = "org.glassfish.json.JsonProviderImpl";
    
    public static JsonProvider provider() {
        final ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class);
        final Iterator<JsonProvider> it = loader.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        try {
            final Class<?> clazz = Class.forName("org.glassfish.json.JsonProviderImpl");
            return (JsonProvider)clazz.newInstance();
        }
        catch (ClassNotFoundException x) {
            throw new JsonException("Provider org.glassfish.json.JsonProviderImpl not found", x);
        }
        catch (Exception x2) {
            throw new JsonException("Provider org.glassfish.json.JsonProviderImpl could not be instantiated: " + x2, x2);
        }
    }
    
    public abstract JsonParser createParser(final Reader p0);
    
    public abstract JsonParser createParser(final InputStream p0);
    
    public abstract JsonParserFactory createParserFactory(final Map<String, ?> p0);
    
    public abstract JsonGenerator createGenerator(final Writer p0);
    
    public abstract JsonGenerator createGenerator(final OutputStream p0);
    
    public abstract JsonGeneratorFactory createGeneratorFactory(final Map<String, ?> p0);
    
    public abstract JsonReader createReader(final Reader p0);
    
    public abstract JsonReader createReader(final InputStream p0);
    
    public abstract JsonWriter createWriter(final Writer p0);
    
    public abstract JsonWriter createWriter(final OutputStream p0);
    
    public abstract JsonWriterFactory createWriterFactory(final Map<String, ?> p0);
    
    public abstract JsonReaderFactory createReaderFactory(final Map<String, ?> p0);
    
    public abstract JsonObjectBuilder createObjectBuilder();
    
    public abstract JsonArrayBuilder createArrayBuilder();
    
    public abstract JsonBuilderFactory createBuilderFactory(final Map<String, ?> p0);
}
