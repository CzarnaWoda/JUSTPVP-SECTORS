package org.glassfish.json;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import org.glassfish.json.api.BufferPool;

class JsonReaderImpl implements JsonReader
{
    private final JsonParserImpl parser;
    private boolean readDone;
    private final BufferPool bufferPool;
    private static /* synthetic */ int[] $SWITCH_TABLE$javax$json$stream$JsonParser$Event;
    
    JsonReaderImpl(final Reader reader, final BufferPool bufferPool) {
        super();
        this.parser = new JsonParserImpl(reader, bufferPool);
        this.bufferPool = bufferPool;
    }
    
    JsonReaderImpl(final InputStream in, final BufferPool bufferPool) {
        super();
        this.parser = new JsonParserImpl(in, bufferPool);
        this.bufferPool = bufferPool;
    }
    
    JsonReaderImpl(final InputStream in, final Charset charset, final BufferPool bufferPool) {
        super();
        this.parser = new JsonParserImpl(in, charset, bufferPool);
        this.bufferPool = bufferPool;
    }
    
    @Override
    public JsonStructure read() {
        if (this.readDone) {
            throw new IllegalStateException(JsonMessages.READER_READ_ALREADY_CALLED());
        }
        this.readDone = true;
        if (this.parser.hasNext()) {
            final JsonParser.Event e = this.parser.next();
            if (e == JsonParser.Event.START_ARRAY) {
                return this.readArray(new JsonArrayBuilderImpl(this.bufferPool));
            }
            if (e == JsonParser.Event.START_OBJECT) {
                return this.readObject(new JsonObjectBuilderImpl(this.bufferPool));
            }
        }
        throw new JsonException("Internal Error");
    }
    
    @Override
    public JsonObject readObject() {
        if (this.readDone) {
            throw new IllegalStateException(JsonMessages.READER_READ_ALREADY_CALLED());
        }
        this.readDone = true;
        if (this.parser.hasNext()) {
            final JsonParser.Event e = this.parser.next();
            if (e == JsonParser.Event.START_OBJECT) {
                return this.readObject(new JsonObjectBuilderImpl(this.bufferPool));
            }
            if (e == JsonParser.Event.START_ARRAY) {
                throw new JsonException(JsonMessages.READER_EXPECTED_OBJECT_GOT_ARRAY());
            }
        }
        throw new JsonException("Internal Error");
    }
    
    @Override
    public JsonArray readArray() {
        if (this.readDone) {
            throw new IllegalStateException(JsonMessages.READER_READ_ALREADY_CALLED());
        }
        this.readDone = true;
        if (this.parser.hasNext()) {
            final JsonParser.Event e = this.parser.next();
            if (e == JsonParser.Event.START_ARRAY) {
                return this.readArray(new JsonArrayBuilderImpl(this.bufferPool));
            }
            if (e == JsonParser.Event.START_OBJECT) {
                throw new JsonException(JsonMessages.READER_EXPECTED_ARRAY_GOT_OBJECT());
            }
        }
        throw new JsonException("Internal Error");
    }
    
    @Override
    public void close() {
        this.readDone = true;
        this.parser.close();
    }
    
    private JsonArray readArray(final JsonArrayBuilder builder) {
        while (this.parser.hasNext()) {
            final JsonParser.Event e = this.parser.next();
            switch ($SWITCH_TABLE$javax$json$stream$JsonParser$Event()[e.ordinal()]) {
                case 1: {
                    final JsonArray array = this.readArray(new JsonArrayBuilderImpl(this.bufferPool));
                    builder.add(array);
                    continue;
                }
                case 2: {
                    final JsonObject object = this.readObject(new JsonObjectBuilderImpl(this.bufferPool));
                    builder.add(object);
                    continue;
                }
                case 4: {
                    builder.add(this.parser.getString());
                    continue;
                }
                case 5: {
                    if (this.parser.isDefinitelyInt()) {
                        builder.add(this.parser.getInt());
                        continue;
                    }
                    builder.add(this.parser.getBigDecimal());
                    continue;
                }
                case 6: {
                    builder.add(JsonValue.TRUE);
                    continue;
                }
                case 7: {
                    builder.add(JsonValue.FALSE);
                    continue;
                }
                case 8: {
                    builder.addNull();
                    continue;
                }
                case 10: {
                    return builder.build();
                }
                default: {
                    throw new JsonException("Internal Error");
                }
            }
        }
        throw new JsonException("Internal Error");
    }
    
    private JsonObject readObject(final JsonObjectBuilder builder) {
        String key = null;
        while (this.parser.hasNext()) {
            final JsonParser.Event e = this.parser.next();
            switch ($SWITCH_TABLE$javax$json$stream$JsonParser$Event()[e.ordinal()]) {
                case 1: {
                    final JsonArray array = this.readArray(new JsonArrayBuilderImpl(this.bufferPool));
                    builder.add(key, array);
                    continue;
                }
                case 2: {
                    final JsonObject object = this.readObject(new JsonObjectBuilderImpl(this.bufferPool));
                    builder.add(key, object);
                    continue;
                }
                case 3: {
                    key = this.parser.getString();
                    continue;
                }
                case 4: {
                    builder.add(key, this.parser.getString());
                    continue;
                }
                case 5: {
                    if (this.parser.isDefinitelyInt()) {
                        builder.add(key, this.parser.getInt());
                        continue;
                    }
                    builder.add(key, this.parser.getBigDecimal());
                    continue;
                }
                case 6: {
                    builder.add(key, JsonValue.TRUE);
                    continue;
                }
                case 7: {
                    builder.add(key, JsonValue.FALSE);
                    continue;
                }
                case 8: {
                    builder.addNull(key);
                    continue;
                }
                case 9: {
                    return builder.build();
                }
                default: {
                    throw new JsonException("Internal Error");
                }
            }
        }
        throw new JsonException("Internal Error");
    }
    
    static /* synthetic */ int[] $SWITCH_TABLE$javax$json$stream$JsonParser$Event() {
        final int[] $switch_TABLE$javax$json$stream$JsonParser$Event = JsonReaderImpl.$SWITCH_TABLE$javax$json$stream$JsonParser$Event;
        if ($switch_TABLE$javax$json$stream$JsonParser$Event != null) {
            return $switch_TABLE$javax$json$stream$JsonParser$Event;
        }
        final int[] $switch_TABLE$javax$json$stream$JsonParser$Event2 = new int[JsonParser.Event.values().length];
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.END_ARRAY.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.END_OBJECT.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError2) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.KEY_NAME.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError3) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.START_ARRAY.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError4) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.START_OBJECT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError5) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.VALUE_FALSE.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError6) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.VALUE_NULL.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError7) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.VALUE_NUMBER.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError8) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.VALUE_STRING.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError9) {}
        try {
            $switch_TABLE$javax$json$stream$JsonParser$Event2[JsonParser.Event.VALUE_TRUE.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError10) {}
        return JsonReaderImpl.$SWITCH_TABLE$javax$json$stream$JsonParser$Event = $switch_TABLE$javax$json$stream$JsonParser$Event2;
    }
}
