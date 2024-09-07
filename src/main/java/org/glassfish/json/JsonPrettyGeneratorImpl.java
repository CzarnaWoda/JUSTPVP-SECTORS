package org.glassfish.json;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.json.stream.JsonGenerator;

import org.glassfish.json.api.BufferPool;

public class JsonPrettyGeneratorImpl extends JsonGeneratorImpl
{
    private int indentLevel;
    
    public JsonPrettyGeneratorImpl(final Writer writer, final BufferPool bufferPool) {
        super(writer, bufferPool);
    }
    
    public JsonPrettyGeneratorImpl(final OutputStream out, final BufferPool bufferPool) {
        super(out, bufferPool);
    }
    
    public JsonPrettyGeneratorImpl(final OutputStream out, final Charset encoding, final BufferPool bufferPool) {
        super(out, encoding, bufferPool);
    }
    
    @Override
    public JsonGenerator writeStartObject() {
        super.writeStartObject();
        ++this.indentLevel;
        return this;
    }
    
    @Override
    public JsonGenerator writeStartObject(final String name) {
        super.writeStartObject(name);
        ++this.indentLevel;
        return this;
    }
    
    @Override
    public JsonGenerator writeStartArray() {
        super.writeStartArray();
        ++this.indentLevel;
        return this;
    }
    
    @Override
    public JsonGenerator writeStartArray(final String name) {
        super.writeStartArray(name);
        ++this.indentLevel;
        return this;
    }
    
    @Override
    public JsonGenerator writeEnd() {
        this.writeNewLine();
        --this.indentLevel;
        this.writeIndent();
        super.writeEnd();
        return this;
    }
    
    private void writeIndent() {
        for (int i = 0; i < this.indentLevel; ++i) {
            this.writeString("    ");
        }
    }
    
    @Override
    protected void writeComma() {
        super.writeComma();
        this.writeChar('\n');
        this.writeIndent();
    }
    
    private void writeNewLine() {
        this.writeChar('\n');
    }
}
