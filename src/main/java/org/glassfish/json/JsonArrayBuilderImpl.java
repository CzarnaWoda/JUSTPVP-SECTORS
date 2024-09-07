package org.glassfish.json;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import org.glassfish.json.api.BufferPool;

class JsonArrayBuilderImpl implements JsonArrayBuilder
{
    private ArrayList<JsonValue> valueList;
    private final BufferPool bufferPool;
    
    JsonArrayBuilderImpl(final BufferPool bufferPool) {
        super();
        this.bufferPool = bufferPool;
    }
    
    @Override
    public JsonArrayBuilder add(final JsonValue value) {
        this.validateValue(value);
        this.addValueList(value);
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final String value) {
        this.validateValue(value);
        this.addValueList(new JsonStringImpl(value));
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final BigDecimal value) {
        this.validateValue(value);
        this.addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final BigInteger value) {
        this.validateValue(value);
        this.addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final int value) {
        this.addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final long value) {
        this.addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final double value) {
        this.addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final boolean value) {
        this.addValueList(value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }
    
    @Override
    public JsonArrayBuilder addNull() {
        this.addValueList(JsonValue.NULL);
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final JsonObjectBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_OBJECT_BUILDER_NULL());
        }
        this.addValueList(builder.build());
        return this;
    }
    
    @Override
    public JsonArrayBuilder add(final JsonArrayBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_ARRAY_BUILDER_NULL());
        }
        this.addValueList(builder.build());
        return this;
    }
    
    @Override
    public JsonArray build() {
        List<JsonValue> snapshot;
        if (this.valueList == null) {
            snapshot = Collections.emptyList();
        }
        else {
            snapshot = Collections.unmodifiableList((List<? extends JsonValue>)this.valueList);
        }
        this.valueList = null;
        return new JsonArrayImpl(snapshot, this.bufferPool);
    }
    
    private void addValueList(final JsonValue value) {
        if (this.valueList == null) {
            this.valueList = new ArrayList<JsonValue>();
        }
        this.valueList.add(value);
    }
    
    private void validateValue(final Object value) {
        if (value == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_VALUE_NULL());
        }
    }
    
    private static final class JsonArrayImpl extends AbstractList<JsonValue> implements JsonArray
    {
        private final List<JsonValue> valueList;
        private final BufferPool bufferPool;
        
        JsonArrayImpl(final List<JsonValue> valueList, final BufferPool bufferPool) {
            super();
            this.valueList = valueList;
            this.bufferPool = bufferPool;
        }
        
        @Override
        public int size() {
            return this.valueList.size();
        }
        
        public JsonObject getJsonObject(int index)
        {
          return (JsonObject)this.valueList.get(index);
        }
        
        public JsonArray getJsonArray(int index)
        {
          return (JsonArray)this.valueList.get(index);
        }
        
        public JsonNumber getJsonNumber(int index)
        {
          return (JsonNumber)this.valueList.get(index);
        }
        
        public JsonString getJsonString(int index)
        {
          return (JsonString)this.valueList.get(index);
        }
        
        @SuppressWarnings("unchecked")
		@Override
        public <T extends JsonValue> List<T> getValuesAs(final Class<T> clazz) {
            return (List<T>)this.valueList;
        }
        
        @Override
        public String getString(final int index) {
            return this.getJsonString(index).getString();
        }
        
        @Override
        public String getString(final int index, final String defaultValue) {
            try {
                return this.getString(index);
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        
        @Override
        public int getInt(final int index) {
            return this.getJsonNumber(index).intValue();
        }
        
        @Override
        public int getInt(final int index, final int defaultValue) {
            try {
                return this.getInt(index);
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        
        @Override
        public boolean getBoolean(final int index) {
            final JsonValue jsonValue = this.get(index);
            if (jsonValue == JsonValue.TRUE) {
                return true;
            }
            if (jsonValue == JsonValue.FALSE) {
                return false;
            }
            throw new ClassCastException();
        }
        
        @Override
        public boolean getBoolean(final int index, final boolean defaultValue) {
            try {
                return this.getBoolean(index);
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        
        @Override
        public boolean isNull(final int index) {
            return this.valueList.get(index).equals(JsonValue.NULL);
        }
        
        @Override
        public ValueType getValueType() {
            return ValueType.ARRAY;
        }
        
        @Override
        public JsonValue get(final int index) {
            return this.valueList.get(index);
        }
        
        @Override
        public String toString() {
            final StringWriter sw = new StringWriter();
            final JsonWriter jw = new JsonWriterImpl(sw, this.bufferPool);
            jw.write(this);
            jw.close();
            return sw.toString();
        }
    }
}
