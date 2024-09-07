package javax.json;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

public class Json
{
    public static JsonParser createParser(final Reader reader) {
        return JsonProvider.provider().createParser(reader);
    }
    
    public static JsonParser createParser(final InputStream in) {
        return JsonProvider.provider().createParser(in);
    }
    
    public static JsonGenerator createGenerator(final Writer writer) {
        return JsonProvider.provider().createGenerator(writer);
    }
    
    public static JsonGenerator createGenerator(final OutputStream out) {
        return JsonProvider.provider().createGenerator(out);
    }
    
    public static JsonParserFactory createParserFactory(final Map<String, ?> config) {
        return JsonProvider.provider().createParserFactory(config);
    }
    
    public static JsonGeneratorFactory createGeneratorFactory(final Map<String, ?> config) {
        return JsonProvider.provider().createGeneratorFactory(config);
    }
    
    public static JsonWriter createWriter(final Writer writer) {
        return JsonProvider.provider().createWriter(writer);
    }
    
    public static JsonWriter createWriter(final OutputStream out) {
        return JsonProvider.provider().createWriter(out);
    }
    
    public static JsonReader createReader(final Reader reader) {
        return JsonProvider.provider().createReader(reader);
    }
    
    public static JsonReader createReader(final InputStream in) {
        return JsonProvider.provider().createReader(in);
    }
    
    public static JsonReaderFactory createReaderFactory(final Map<String, ?> config) {
        return JsonProvider.provider().createReaderFactory(config);
    }
    
    public static JsonWriterFactory createWriterFactory(final Map<String, ?> config) {
        return JsonProvider.provider().createWriterFactory(config);
    }
    
    public static JsonArrayBuilder createArrayBuilder() {
        return JsonProvider.provider().createArrayBuilder();
    }
    
    public static JsonObjectBuilder createObjectBuilder() {
        return JsonProvider.provider().createObjectBuilder();
    }
    
    public static JsonBuilderFactory createBuilderFactory(final Map<String, ?> config) {
        return JsonProvider.provider().createBuilderFactory(config);
    }
}
