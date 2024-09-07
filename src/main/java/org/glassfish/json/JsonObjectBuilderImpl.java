package org.glassfish.json;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import org.glassfish.json.api.BufferPool;

class JsonObjectBuilderImpl implements JsonObjectBuilder
{
    private Map<String, JsonValue> valueMap;
    private final BufferPool bufferPool;
    
    JsonObjectBuilderImpl(final BufferPool bufferPool) {
        super();
        this.bufferPool = bufferPool;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final JsonValue value) {
        this.validateName(name);
        this.validateValue(value);
        this.putValueMap(name, value);
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final String value) {
        this.validateName(name);
        this.validateValue(value);
        this.putValueMap(name, new JsonStringImpl(value));
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final BigInteger value) {
        this.validateName(name);
        this.validateValue(value);
        this.putValueMap(name, JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final BigDecimal value) {
        this.validateName(name);
        this.validateValue(value);
        this.putValueMap(name, JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final int value) {
        this.validateName(name);
        this.putValueMap(name, JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final long value) {
        this.validateName(name);
        this.putValueMap(name, JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final double value) {
        this.validateName(name);
        this.putValueMap(name, JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final boolean value) {
        this.validateName(name);
        this.putValueMap(name, value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }
    
    @Override
    public JsonObjectBuilder addNull(final String name) {
        this.validateName(name);
        this.putValueMap(name, JsonValue.NULL);
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final JsonObjectBuilder builder) {
        this.validateName(name);
        if (builder == null) {
            throw new NullPointerException(JsonMessages.OBJBUILDER_OBJECT_BUILDER_NULL());
        }
        this.putValueMap(name, builder.build());
        return this;
    }
    
    @Override
    public JsonObjectBuilder add(final String name, final JsonArrayBuilder builder) {
        this.validateName(name);
        if (builder == null) {
            throw new NullPointerException(JsonMessages.OBJBUILDER_ARRAY_BUILDER_NULL());
        }
        this.putValueMap(name, builder.build());
        return this;
    }
    
    @Override
    public JsonObject build() {
        final Map<String, JsonValue> snapshot = (this.valueMap == null) ? Collections.emptyMap() : Collections.unmodifiableMap((Map<? extends String, ? extends JsonValue>)this.valueMap);
        this.valueMap = null;
        return new JsonObjectImpl(snapshot, this.bufferPool);
    }
    
    private void putValueMap(final String name, final JsonValue value) {
        if (this.valueMap == null) {
            this.valueMap = new LinkedHashMap<String, JsonValue>();
        }
        this.valueMap.put(name, value);
    }
    
    private void validateName(final String name) {
        if (name == null) {
            throw new NullPointerException(JsonMessages.OBJBUILDER_NAME_NULL());
        }
    }
    
    private void validateValue(final Object value) {
        if (value == null) {
            throw new NullPointerException(JsonMessages.OBJBUILDER_VALUE_NULL());
        }
    }
    

    private static final class JsonObjectImpl
      extends AbstractMap<String, JsonValue>
      implements JsonObject
    {
      private final Map<String, JsonValue> valueMap;
      private final BufferPool bufferPool;
      
      JsonObjectImpl(Map<String, JsonValue> valueMap, BufferPool bufferPool)
      {
        this.valueMap = valueMap;
        this.bufferPool = bufferPool;
      }
      
      public JsonArray getJsonArray(String name)
      {
        return (JsonArray)get(name);
      }
      
      public JsonObject getJsonObject(String name)
      {
        return (JsonObject)get(name);
      }
      
      public JsonNumber getJsonNumber(String name)
      {
        return (JsonNumber)get(name);
      }
      
      public JsonString getJsonString(String name)
      {
        return (JsonString)get(name);
      }
      
      public String getString(String name)
      {
        return getJsonString(name).getString();
      }
      
      public String getString(String name, String defaultValue)
      {
        try
        {
          return getString(name);
        }
        catch (Exception e) {}
        return defaultValue;
      }
      
      public int getInt(String name)
      {
        return getJsonNumber(name).intValue();
      }
      
      public int getInt(String name, int defaultValue)
      {
        try
        {
          return getInt(name);
        }
        catch (Exception e) {}
        return defaultValue;
      }
      
      public boolean getBoolean(String name)
      {
        JsonValue value = (JsonValue)get(name);
        if (value == null) {
          throw new NullPointerException();
        }
        if (value == JsonValue.TRUE) {
          return true;
        }
        if (value == JsonValue.FALSE) {
          return false;
        }
        throw new ClassCastException();
      }
      
      public boolean getBoolean(String name, boolean defaultValue)
      {
        try
        {
          return getBoolean(name);
        }
        catch (Exception e) {}
        return defaultValue;
      }
      
      public boolean isNull(String name)
      {
        return ((JsonValue)get(name)).equals(JsonValue.NULL);
      }
      
      public ValueType getValueType()
      {
        return ValueType.OBJECT;
      }
      
      public Set<Entry<String, JsonValue>> entrySet()
      {
        return this.valueMap.entrySet();
      }
      
      public String toString()
      {
        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriterImpl(sw, this.bufferPool);
        jw.write(this);
        jw.close();
        return sw.toString();
      }
    }
  }

