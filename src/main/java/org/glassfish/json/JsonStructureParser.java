package org.glassfish.json;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;

class JsonStructureParser implements JsonParser
{
    private Scope current;
    private Event state;
    private final Deque<Scope> scopeStack;
    private static /* synthetic */ int[] $SWITCH_TABLE$javax$json$JsonValue$ValueType;
    
    JsonStructureParser(final JsonArray array) {
        super();
        this.scopeStack = new ArrayDeque<Scope>();
        this.current = new ArrayScope(array);
    }
    
    JsonStructureParser(final JsonObject object) {
        super();
        this.scopeStack = new ArrayDeque<Scope>();
        this.current = new ObjectScope(object);
    }
    
    @Override
    public String getString() {
        if (this.state == Event.KEY_NAME) {
            return ((ObjectScope)this.current).key;
        }
        if (this.state == Event.VALUE_STRING) {
            return ((JsonString)this.current.getJsonValue()).getString();
        }
        throw new IllegalStateException(JsonMessages.PARSER_GETSTRING_ERR(this.state));
    }
    
    @Override
    public boolean isIntegralNumber() {
        if (this.state == Event.VALUE_NUMBER) {
            return ((JsonNumber)this.current.getJsonValue()).isIntegral();
        }
        throw new IllegalStateException(JsonMessages.PARSER_ISINTEGRALNUMBER_ERR(this.state));
    }
    
    @Override
    public int getInt() {
        if (this.state == Event.VALUE_NUMBER) {
            return ((JsonNumber)this.current.getJsonValue()).intValue();
        }
        throw new IllegalStateException(JsonMessages.PARSER_GETINT_ERR(this.state));
    }
    
    @Override
    public long getLong() {
        if (this.state == Event.VALUE_NUMBER) {
            return ((JsonNumber)this.current.getJsonValue()).longValue();
        }
        throw new IllegalStateException(JsonMessages.PARSER_GETLONG_ERR(this.state));
    }
    
    @Override
    public BigDecimal getBigDecimal() {
        if (this.state == Event.VALUE_NUMBER) {
            return ((JsonNumber)this.current.getJsonValue()).bigDecimalValue();
        }
        throw new IllegalStateException(JsonMessages.PARSER_GETBIGDECIMAL_ERR(this.state));
    }
    
    @Override
    public JsonLocation getLocation() {
        return JsonLocationImpl.UNKNOWN;
    }
    
    @Override
    public boolean hasNext() {
        return (this.state != Event.END_OBJECT && this.state != Event.END_ARRAY) || !this.scopeStack.isEmpty();
    }
    
    @Override
    public Event next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.transition();
        return this.state;
    }
    
    private void transition() {
        if (this.state == null) {
            this.state = ((this.current instanceof ArrayScope) ? Event.START_ARRAY : Event.START_OBJECT);
        }
        else {
            if (this.state == Event.END_OBJECT || this.state == Event.END_ARRAY) {
                this.current = this.scopeStack.pop();
            }
            if (this.current instanceof ArrayScope) {
                if (this.current.hasNext()) {
                    this.current.next();
                    this.state = getState(this.current.getJsonValue());
                    if (this.state == Event.START_ARRAY || this.state == Event.START_OBJECT) {
                        this.scopeStack.push(this.current);
                        this.current = Scope.createScope(this.current.getJsonValue());
                    }
                }
                else {
                    this.state = Event.END_ARRAY;
                }
            }
            else if (this.state == Event.KEY_NAME) {
                this.state = getState(this.current.getJsonValue());
                if (this.state == Event.START_ARRAY || this.state == Event.START_OBJECT) {
                    this.scopeStack.push(this.current);
                    this.current = Scope.createScope(this.current.getJsonValue());
                }
            }
            else if (this.current.hasNext()) {
                this.current.next();
                this.state = Event.KEY_NAME;
            }
            else {
                this.state = Event.END_OBJECT;
            }
        }
    }
    
    @Override
    public void close() {
    }
    
    private static Event getState(final JsonValue value) {
        switch ($SWITCH_TABLE$javax$json$JsonValue$ValueType()[value.getValueType().ordinal()]) {
            case 1: {
                return Event.START_ARRAY;
            }
            case 2: {
                return Event.START_OBJECT;
            }
            case 3: {
                return Event.VALUE_STRING;
            }
            case 4: {
                return Event.VALUE_NUMBER;
            }
            case 5: {
                return Event.VALUE_TRUE;
            }
            case 6: {
                return Event.VALUE_FALSE;
            }
            case 7: {
                return Event.VALUE_NULL;
            }
            default: {
                throw new JsonException("Unknown value type=" + value.getValueType());
            }
        }
    }
    
    static /* synthetic */ int[] $SWITCH_TABLE$javax$json$JsonValue$ValueType() {
        final int[] $switch_TABLE$javax$json$JsonValue$ValueType = JsonStructureParser.$SWITCH_TABLE$javax$json$JsonValue$ValueType;
        if ($switch_TABLE$javax$json$JsonValue$ValueType != null) {
            return $switch_TABLE$javax$json$JsonValue$ValueType;
        }
        final int[] $switch_TABLE$javax$json$JsonValue$ValueType2 = new int[JsonValue.ValueType.values().length];
        try {
            $switch_TABLE$javax$json$JsonValue$ValueType2[JsonValue.ValueType.ARRAY.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            $switch_TABLE$javax$json$JsonValue$ValueType2[JsonValue.ValueType.FALSE.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError2) {}
        try {
            $switch_TABLE$javax$json$JsonValue$ValueType2[JsonValue.ValueType.NULL.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError3) {}
        try {
            $switch_TABLE$javax$json$JsonValue$ValueType2[JsonValue.ValueType.NUMBER.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError4) {}
        try {
            $switch_TABLE$javax$json$JsonValue$ValueType2[JsonValue.ValueType.OBJECT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError5) {}
        try {
            $switch_TABLE$javax$json$JsonValue$ValueType2[JsonValue.ValueType.STRING.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError6) {}
        try {
            $switch_TABLE$javax$json$JsonValue$ValueType2[JsonValue.ValueType.TRUE.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError7) {}
        return JsonStructureParser.$SWITCH_TABLE$javax$json$JsonValue$ValueType = $switch_TABLE$javax$json$JsonValue$ValueType2;
    }
    
    private static class ArrayScope extends Scope
    {
        private final Iterator<JsonValue> it;
        private JsonValue value;
        
        ArrayScope(final JsonArray array) {
            super();
            this.it = array.iterator();
        }
        
        @Override
        public boolean hasNext() {
            return this.it.hasNext();
        }
        
        @Override
        public JsonValue next() {
            return this.value = this.it.next();
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        JsonValue getJsonValue() {
            return this.value;
        }
    }
    
    private static class ObjectScope extends Scope
    {
        private final Iterator<Map.Entry<String, JsonValue>> it;
        private JsonValue value;
        private String key;
        
        ObjectScope(final JsonObject object) {
            super();
            this.it = object.entrySet().iterator();
        }
        
        @Override
        public boolean hasNext() {
            return this.it.hasNext();
        }
        
        @Override
        public Map.Entry<String, JsonValue> next() {
            final Map.Entry<String, JsonValue> next = this.it.next();
            this.key = next.getKey();
            this.value = next.getValue();
            return next;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        JsonValue getJsonValue() {
            return this.value;
        }
    }
    
    @SuppressWarnings("rawtypes")
	private abstract static class Scope implements Iterator
    {
        abstract JsonValue getJsonValue();
        
        static Scope createScope(final JsonValue value) {
            if (value instanceof JsonArray) {
                return new ArrayScope((JsonArray)value);
            }
            if (value instanceof JsonObject) {
                return new ObjectScope((JsonObject)value);
            }
            throw new JsonException("Cannot be called for value=" + value);
        }
    }
}
