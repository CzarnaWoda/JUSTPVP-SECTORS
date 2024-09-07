package org.glassfish.json;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import org.glassfish.json.api.BufferPool;

class JsonParserFactoryImpl implements JsonParserFactory
{
    private final Map<String, ?> config;
    private final BufferPool bufferPool;
    
    JsonParserFactoryImpl(final BufferPool bufferPool) {
        super();
        this.config = Collections.emptyMap();
        this.bufferPool = bufferPool;
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
    public JsonParser createParser(final InputStream in, final Charset charset) {
        return new JsonParserImpl(in, charset, this.bufferPool);
    }
    
    @Override
    public JsonParser createParser(final JsonArray array) {
        return new JsonStructureParser(array);
    }
    
    @Override
    public Map<String, ?> getConfigInUse() {
        return this.config;
    }
    
    @Override
    public JsonParser createParser(final JsonObject object) {
        return new JsonStructureParser(object);
    }
}
