package org.glassfish.json;

import java.util.Collections;
import java.util.Map;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

import org.glassfish.json.api.BufferPool;

class JsonBuilderFactoryImpl implements JsonBuilderFactory
{
    private final Map<String, ?> config;
    private final BufferPool bufferPool;
    
    JsonBuilderFactoryImpl(final BufferPool bufferPool) {
        super();
        this.config = Collections.emptyMap();
        this.bufferPool = bufferPool;
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
    public Map<String, ?> getConfigInUse() {
        return this.config;
    }
}
