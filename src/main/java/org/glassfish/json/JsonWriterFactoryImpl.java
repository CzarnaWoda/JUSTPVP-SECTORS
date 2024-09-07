package org.glassfish.json;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;

import org.glassfish.json.api.BufferPool;

class JsonWriterFactoryImpl implements JsonWriterFactory
{
    private final Map<String, ?> config;
    private final boolean prettyPrinting;
    private final BufferPool bufferPool;
    
    JsonWriterFactoryImpl(final Map<String, ?> config, final boolean prettyPrinting, final BufferPool bufferPool) {
        super();
        this.config = config;
        this.prettyPrinting = prettyPrinting;
        this.bufferPool = bufferPool;
    }
    
    @Override
    public JsonWriter createWriter(final Writer writer) {
        return new JsonWriterImpl(writer, this.prettyPrinting, this.bufferPool);
    }
    
    @Override
    public JsonWriter createWriter(final OutputStream out) {
        return new JsonWriterImpl(out, this.prettyPrinting, this.bufferPool);
    }
    
    @Override
    public JsonWriter createWriter(final OutputStream out, final Charset charset) {
        return new JsonWriterImpl(out, charset, this.prettyPrinting, this.bufferPool);
    }
    
    @Override
    public Map<String, ?> getConfigInUse() {
        return this.config;
    }
}
