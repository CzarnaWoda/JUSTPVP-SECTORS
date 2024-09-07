package org.glassfish.json;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import org.glassfish.json.api.BufferPool;

class JsonWriterImpl implements JsonWriter
{
    private static final Charset UTF_8;
    private final JsonGeneratorImpl generator;
    private boolean writeDone;
    private final NoFlushOutputStream os;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
    }
    
    JsonWriterImpl(final Writer writer, final BufferPool bufferPool) {
        this(writer, false, bufferPool);
    }
    
    JsonWriterImpl(final Writer writer, final boolean prettyPrinting, final BufferPool bufferPool) {
        super();
        this.generator = (prettyPrinting ? new JsonPrettyGeneratorImpl(writer, bufferPool) : new JsonGeneratorImpl(writer, bufferPool));
        this.os = null;
    }
    
    JsonWriterImpl(final OutputStream out, final BufferPool bufferPool) {
        this(out, JsonWriterImpl.UTF_8, false, bufferPool);
    }
    
    JsonWriterImpl(final OutputStream out, final boolean prettyPrinting, final BufferPool bufferPool) {
        this(out, JsonWriterImpl.UTF_8, prettyPrinting, bufferPool);
    }
    
    JsonWriterImpl(final OutputStream out, final Charset charset, final boolean prettyPrinting, final BufferPool bufferPool) {
        super();
        this.os = new NoFlushOutputStream(out);
        this.generator = (prettyPrinting ? new JsonPrettyGeneratorImpl(this.os, charset, bufferPool) : new JsonGeneratorImpl(this.os, charset, bufferPool));
    }
    
    @Override
    public void writeArray(final JsonArray array) {
        if (this.writeDone) {
            throw new IllegalStateException(JsonMessages.WRITER_WRITE_ALREADY_CALLED());
        }
        this.writeDone = true;
        this.generator.writeStartArray();
        for (final JsonValue value : array) {
            this.generator.write(value);
        }
        this.generator.writeEnd();
        this.generator.flushBuffer();
        if (this.os != null) {
            this.generator.flush();
        }
    }
    
    @Override
    public void writeObject(final JsonObject object) {
        if (this.writeDone) {
            throw new IllegalStateException(JsonMessages.WRITER_WRITE_ALREADY_CALLED());
        }
        this.writeDone = true;
        this.generator.writeStartObject();
        for (final Map.Entry<String, JsonValue> e : object.entrySet()) {
            this.generator.write(e.getKey(), e.getValue());
        }
        this.generator.writeEnd();
        this.generator.flushBuffer();
        if (this.os != null) {
            this.generator.flush();
        }
    }
    
    @Override
    public void write(final JsonStructure value) {
        if (value instanceof JsonArray) {
            this.writeArray((JsonArray)value);
        }
        else {
            this.writeObject((JsonObject)value);
        }
    }
    
    @Override
    public void close() {
        this.writeDone = true;
        this.generator.close();
    }
    
    private static final class NoFlushOutputStream extends FilterOutputStream
    {
        public NoFlushOutputStream(final OutputStream out) {
            super(out);
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            this.out.write(b, off, len);
        }
        
        @Override
        public void flush() {
        }
    }
}
