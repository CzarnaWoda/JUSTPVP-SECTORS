package org.glassfish.json;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import org.glassfish.json.api.BufferPool;

public class JsonProviderImpl extends JsonProvider
{
    private final BufferPool bufferPool;
    
    public JsonProviderImpl() {
        super();
        this.bufferPool = new BufferPoolImpl();
    }
    
    @Override
    public JsonGenerator createGenerator(final Writer writer) {
        return new JsonGeneratorImpl(writer, this.bufferPool);
    }
    
    @Override
    public JsonGenerator createGenerator(final OutputStream out) {
        return new JsonGeneratorImpl(out, this.bufferPool);
    }
    
    @Override
    public JsonParser createParser(final Reader reader) {
        return new JsonParserImpl(reader, this.bufferPool);
    }
    
    @Override
    public JsonParser createParser(final InputStream in) {
        return new JsonParserImpl(in, this.bufferPool);
    }
    
    @Override
    public JsonParserFactory createParserFactory(final Map<String, ?> config) {
        BufferPool pool = null;
        if (config != null && config.containsKey(BufferPool.class.getName())) {
            pool = (BufferPool)config.get(BufferPool.class.getName());
        }
        if (pool == null) {
            pool = this.bufferPool;
        }
        return new JsonParserFactoryImpl(pool);
    }
    
    @Override
    public JsonGeneratorFactory createGeneratorFactory(final Map<String, ?> config) {
        Map<String, Object> providerConfig;
        boolean prettyPrinting;
        BufferPool pool;
        if (config == null) {
            providerConfig = Collections.emptyMap();
            prettyPrinting = false;
            pool = this.bufferPool;
        }
        else {
            providerConfig = new HashMap<String, Object>();
            if (prettyPrinting = isPrettyPrintingEnabled(config)) {
                providerConfig.put("javax.json.stream.JsonGenerator.prettyPrinting", true);
            }
            pool = (BufferPool)config.get(BufferPool.class.getName());
            if (pool != null) {
                providerConfig.put(BufferPool.class.getName(), pool);
            }
            else {
                pool = this.bufferPool;
            }
            providerConfig = Collections.unmodifiableMap((Map<? extends String, ?>)providerConfig);
        }
        return new JsonGeneratorFactoryImpl(providerConfig, prettyPrinting, pool);
    }
    
    @Override
    public JsonReader createReader(final Reader reader) {
        return new JsonReaderImpl(reader, this.bufferPool);
    }
    
    @Override
    public JsonReader createReader(final InputStream in) {
        return new JsonReaderImpl(in, this.bufferPool);
    }
    
    @Override
    public JsonWriter createWriter(final Writer writer) {
        return new JsonWriterImpl(writer, this.bufferPool);
    }
    
    @Override
    public JsonWriter createWriter(final OutputStream out) {
        return new JsonWriterImpl(out, this.bufferPool);
    }
    
    @Override
    public JsonWriterFactory createWriterFactory(final Map<String, ?> config) {
        Map<String, Object> providerConfig;
        boolean prettyPrinting;
        BufferPool pool;
        if (config == null) {
            providerConfig = Collections.emptyMap();
            prettyPrinting = false;
            pool = this.bufferPool;
        }
        else {
            providerConfig = new HashMap<String, Object>();
            if (prettyPrinting = isPrettyPrintingEnabled(config)) {
                providerConfig.put("javax.json.stream.JsonGenerator.prettyPrinting", true);
            }
            pool = (BufferPool)config.get(BufferPool.class.getName());
            if (pool != null) {
                providerConfig.put(BufferPool.class.getName(), pool);
            }
            else {
                pool = this.bufferPool;
            }
            providerConfig = Collections.unmodifiableMap((Map<? extends String, ?>)providerConfig);
        }
        return new JsonWriterFactoryImpl(providerConfig, prettyPrinting, pool);
    }
    
    @Override
    public JsonReaderFactory createReaderFactory(final Map<String, ?> config) {
        BufferPool pool = null;
        if (config != null && config.containsKey(BufferPool.class.getName())) {
            pool = (BufferPool)config.get(BufferPool.class.getName());
        }
        if (pool == null) {
            pool = this.bufferPool;
        }
        return new JsonReaderFactoryImpl(pool);
    }
    
    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return new JsonObjectBuilderImpl(this.bufferPool);
    }
    
    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return new JsonArrayBuilderImpl(this.bufferPool);
    }
    
    @Override
    public JsonBuilderFactory createBuilderFactory(final Map<String, ?> config) {
        BufferPool pool = null;
        if (config != null && config.containsKey(BufferPool.class.getName())) {
            pool = (BufferPool)config.get(BufferPool.class.getName());
        }
        if (pool == null) {
            pool = this.bufferPool;
        }
        return new JsonBuilderFactoryImpl(pool);
    }
    
    static boolean isPrettyPrintingEnabled(final Map<String, ?> config) {
        return config.containsKey("javax.json.stream.JsonGenerator.prettyPrinting");
    }
}
