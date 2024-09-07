package org.glassfish.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerationException;
import javax.json.stream.JsonGenerator;

import org.glassfish.json.api.BufferPool;

class JsonGeneratorImpl implements JsonGenerator
{
    private static final Charset UTF_8;
    private static final char[] INT_MIN_VALUE_CHARS;
    private static final int[] INT_CHARS_SIZE_TABLE;
    private static final char[] DIGIT_TENS;
    private static final char[] DIGIT_ONES;
    private static final char[] DIGITS;
    private final BufferPool bufferPool;
    private final Writer writer;
    private Context currentContext;
    private final Deque<Context> stack;
    private final char[] buf;
    private int len;
    private static /* synthetic */ int[] $SWITCH_TABLE$javax$json$JsonValue$ValueType;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
        INT_MIN_VALUE_CHARS = "-2147483648".toCharArray();
        INT_CHARS_SIZE_TABLE = new int[] { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };
        DIGIT_TENS = new char[] { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9' };
        DIGIT_ONES = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    }
    
    JsonGeneratorImpl(final Writer writer, final BufferPool bufferPool) {
        super();
        this.currentContext = new Context(Scope.IN_NONE);
        this.stack = new ArrayDeque<Context>();
        this.len = 0;
        this.writer = writer;
        this.bufferPool = bufferPool;
        this.buf = bufferPool.take();
    }
    
    JsonGeneratorImpl(final OutputStream out, final BufferPool bufferPool) {
        this(out, JsonGeneratorImpl.UTF_8, bufferPool);
    }
    
    JsonGeneratorImpl(final OutputStream out, final Charset encoding, final BufferPool bufferPool) {
        this(new OutputStreamWriter(out, encoding), bufferPool);
    }
    
    @Override
    public void flush() {
        this.flushBuffer();
        try {
            this.writer.flush();
        }
        catch (IOException ioe) {
            throw new JsonException(JsonMessages.GENERATOR_FLUSH_IO_ERR(), ioe);
        }
    }
    
    @Override
    public JsonGenerator writeStartObject() {
        if (this.currentContext.scope == Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        if (this.currentContext.scope == Scope.IN_NONE && !this.currentContext.first) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_MULTIPLE_TEXT());
        }
        this.writeComma();
        this.writeChar('{');
        this.stack.push(this.currentContext);
        this.currentContext = new Context(Scope.IN_OBJECT);
        return this;
    }
    
    @Override
    public JsonGenerator writeStartObject(final String name) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeChar('{');
        this.stack.push(this.currentContext);
        this.currentContext = new Context(Scope.IN_OBJECT);
        return this;
    }
    
    private JsonGenerator writeName(final String name) {
        this.writeComma();
        this.writeEscapedString(name);
        this.writeChar(':');
        return this;
    }
    
    @Override
    public JsonGenerator write(final String name, final String fieldValue) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeEscapedString(fieldValue);
        return this;
    }
    
    @Override
    public JsonGenerator write(final String name, final int value) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeInt(value);
        return this;
    }
    
    @Override
    public JsonGenerator write(final String name, final long value) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeString(String.valueOf(value));
        return this;
    }
    
    @Override
    public JsonGenerator write(final String name, final double value) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            throw new NumberFormatException(JsonMessages.GENERATOR_DOUBLE_INFINITE_NAN());
        }
        this.writeName(name);
        this.writeString(String.valueOf(value));
        return this;
    }
    
    @Override
    public JsonGenerator write(final String name, final BigInteger value) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeString(String.valueOf(value));
        return this;
    }
    
    @Override
    public JsonGenerator write(final String name, final BigDecimal value) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeString(String.valueOf(value));
        return this;
    }
    
    @Override
    public JsonGenerator write(final String name, final boolean value) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeString(value ? "true" : "false");
        return this;
    }
    
    @Override
    public JsonGenerator writeNull(final String name) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeString("null");
        return this;
    }
    
    @Override
    public JsonGenerator write(final JsonValue value) {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        switch ($SWITCH_TABLE$javax$json$JsonValue$ValueType()[value.getValueType().ordinal()]) {
            case 1: {
                final JsonArray array = (JsonArray)value;
                this.writeStartArray();
                for (final JsonValue child : array) {
                    this.write(child);
                }
                this.writeEnd();
                break;
            }
            case 2: {
                final JsonObject object = (JsonObject)value;
                this.writeStartObject();
                for (final Map.Entry<String, JsonValue> member : object.entrySet()) {
                    this.write(member.getKey(), member.getValue());
                }
                this.writeEnd();
                break;
            }
            case 3: {
                final JsonString str = (JsonString)value;
                this.write(str.getString());
                break;
            }
            case 4: {
                final JsonNumber number = (JsonNumber)value;
                this.writeValue(number.toString());
                break;
            }
            case 5: {
                this.write(true);
                break;
            }
            case 6: {
                this.write(false);
                break;
            }
            case 7: {
                this.writeNull();
                break;
            }
        }
        return this;
    }
    
    @Override
    public JsonGenerator writeStartArray() {
        if (this.currentContext.scope == Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        if (this.currentContext.scope == Scope.IN_NONE && !this.currentContext.first) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_MULTIPLE_TEXT());
        }
        this.writeComma();
        this.writeChar('[');
        this.stack.push(this.currentContext);
        this.currentContext = new Context(Scope.IN_ARRAY);
        return this;
    }
    
    @Override
    public JsonGenerator writeStartArray(final String name) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeName(name);
        this.writeChar('[');
        this.stack.push(this.currentContext);
        this.currentContext = new Context(Scope.IN_ARRAY);
        return this;
    }
    
    @Override
    public JsonGenerator write(final String name, final JsonValue value) {
        if (this.currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        switch ($SWITCH_TABLE$javax$json$JsonValue$ValueType()[value.getValueType().ordinal()]) {
            case 1: {
                final JsonArray array = (JsonArray)value;
                this.writeStartArray(name);
                for (final JsonValue child : array) {
                    this.write(child);
                }
                this.writeEnd();
                break;
            }
            case 2: {
                final JsonObject object = (JsonObject)value;
                this.writeStartObject(name);
                for (final Map.Entry<String, JsonValue> member : object.entrySet()) {
                    this.write(member.getKey(), member.getValue());
                }
                this.writeEnd();
                break;
            }
            case 3: {
                final JsonString str = (JsonString)value;
                this.write(name, str.getString());
                break;
            }
            case 4: {
                final JsonNumber number = (JsonNumber)value;
                this.writeValue(name, number.toString());
                break;
            }
            case 5: {
                this.write(name, true);
                break;
            }
            case 6: {
                this.write(name, false);
                break;
            }
            case 7: {
                this.writeNull(name);
                break;
            }
        }
        return this;
    }
    
    @Override
    public JsonGenerator write(final String value) {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeComma();
        this.writeEscapedString(value);
        return this;
    }
    
    @Override
    public JsonGenerator write(final int value) {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeComma();
        this.writeInt(value);
        return this;
    }
    
    @Override
    public JsonGenerator write(final long value) {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeValue(String.valueOf(value));
        return this;
    }
    
    @Override
    public JsonGenerator write(final double value) {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            throw new NumberFormatException(JsonMessages.GENERATOR_DOUBLE_INFINITE_NAN());
        }
        this.writeValue(String.valueOf(value));
        return this;
    }
    
    @Override
    public JsonGenerator write(final BigInteger value) {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeValue(value.toString());
        return this;
    }
    
    @Override
    public JsonGenerator write(final BigDecimal value) {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeValue(value.toString());
        return this;
    }
    
    @Override
    public JsonGenerator write(final boolean value) {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeComma();
        this.writeString(value ? "true" : "false");
        return this;
    }
    
    @Override
    public JsonGenerator writeNull() {
        if (this.currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_ILLEGAL_METHOD(this.currentContext.scope));
        }
        this.writeComma();
        this.writeString("null");
        return this;
    }
    
    private void writeValue(final String value) {
        this.writeComma();
        this.writeString(value);
    }
    
    private void writeValue(final String name, final String value) {
        this.writeComma();
        this.writeEscapedString(name);
        this.writeChar(':');
        this.writeString(value);
    }
    
    @Override
    public JsonGenerator writeEnd() {
        if (this.currentContext.scope == Scope.IN_NONE) {
            throw new JsonGenerationException("writeEnd() cannot be called in no context");
        }
        this.writeChar((this.currentContext.scope == Scope.IN_ARRAY) ? ']' : '}');
        this.currentContext = this.stack.pop();
        return this;
    }
    
    protected void writeComma() {
        if (!this.currentContext.first) {
            this.writeChar(',');
        }
        this.currentContext.first = false;
    }
    
    @Override
    public void close() {
        if (this.currentContext.scope != Scope.IN_NONE || this.currentContext.first) {
            throw new JsonGenerationException(JsonMessages.GENERATOR_INCOMPLETE_JSON());
        }
        this.flushBuffer();
        try {
            this.writer.close();
        }
        catch (IOException ioe) {
            throw new JsonException(JsonMessages.GENERATOR_CLOSE_IO_ERR(), ioe);
        }
        this.bufferPool.recycle(this.buf);
    }
    
    void writeEscapedString(final String string) {
        this.writeChar('\"');
        for (int len = string.length(), i = 0; i < len; ++i) {
            final int begin = i;
            int end = i;
            char c;
            for (c = string.charAt(i); c >= ' ' && c <= 1114111 && c != '\"' && c != '\\'; c = string.charAt(i)) {
                end = ++i;
                if (i >= len) {
                    break;
                }
            }
            if (begin < end) {
                this.writeString(string, begin, end);
                if (i == len) {
                    break;
                }
            }
            switch (c) {
                case '\"':
                case '\\': {
                    this.writeChar('\\');
                    this.writeChar(c);
                    break;
                }
                case '\b': {
                    this.writeChar('\\');
                    this.writeChar('b');
                    break;
                }
                case '\f': {
                    this.writeChar('\\');
                    this.writeChar('f');
                    break;
                }
                case '\n': {
                    this.writeChar('\\');
                    this.writeChar('n');
                    break;
                }
                case '\r': {
                    this.writeChar('\\');
                    this.writeChar('r');
                    break;
                }
                case '\t': {
                    this.writeChar('\\');
                    this.writeChar('t');
                    break;
                }
                default: {
                    final String hex = "000" + Integer.toHexString(c);
                    this.writeString("\\u" + hex.substring(hex.length() - 4));
                    break;
                }
            }
        }
        this.writeChar('\"');
    }
    
    void writeString(final String str, int begin, final int end) {
        while (begin < end) {
            final int no = Math.min(this.buf.length - this.len, end - begin);
            str.getChars(begin, begin + no, this.buf, this.len);
            begin += no;
            this.len += no;
            if (this.len >= this.buf.length) {
                this.flushBuffer();
            }
        }
    }
    
    void writeString(final String str) {
        this.writeString(str, 0, str.length());
    }
    
    void writeChar(final char c) {
        if (this.len >= this.buf.length) {
            this.flushBuffer();
        }
        this.buf[this.len++] = c;
    }
    
    void writeInt(final int num) {
        int size;
        if (num == Integer.MIN_VALUE) {
            size = JsonGeneratorImpl.INT_MIN_VALUE_CHARS.length;
        }
        else {
            size = ((num < 0) ? (stringSize(-num) + 1) : stringSize(num));
        }
        if (this.len + size >= this.buf.length) {
            this.flushBuffer();
        }
        if (num == Integer.MIN_VALUE) {
            System.arraycopy(JsonGeneratorImpl.INT_MIN_VALUE_CHARS, 0, this.buf, this.len, size);
        }
        else {
            fillIntChars(num, this.buf, this.len + size);
        }
        this.len += size;
    }
    
    void flushBuffer() {
        try {
            if (this.len > 0) {
                this.writer.write(this.buf, 0, this.len);
                this.len = 0;
            }
        }
        catch (IOException ioe) {
            throw new JsonException(JsonMessages.GENERATOR_WRITE_IO_ERR(), ioe);
        }
    }
    
    private static int stringSize(final int x) {
        int i;
        for (i = 0; x > JsonGeneratorImpl.INT_CHARS_SIZE_TABLE[i]; ++i) {}
        return i + 1;
    }
    
    private static void fillIntChars(int i, final char[] buf, int index) {
        char sign = '\0';
        if (i < 0) {
            sign = '-';
            i = -i;
        }
        while (i >= 65536) {
            final int q = i / 100;
            final int r = i - ((q << 6) + (q << 5) + (q << 2));
            i = q;
            buf[--index] = JsonGeneratorImpl.DIGIT_ONES[r];
            buf[--index] = JsonGeneratorImpl.DIGIT_TENS[r];
        }
        do {
            final int q = i * 52429 >>> 19;
            final int r = i - ((q << 3) + (q << 1));
            buf[--index] = JsonGeneratorImpl.DIGITS[r];
            i = q;
        } while (i != 0);
        if (sign != '\0') {
            buf[--index] = sign;
        }
    }
    
    static /* synthetic */ int[] $SWITCH_TABLE$javax$json$JsonValue$ValueType() {
        final int[] $switch_TABLE$javax$json$JsonValue$ValueType = JsonGeneratorImpl.$SWITCH_TABLE$javax$json$JsonValue$ValueType;
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
        return JsonGeneratorImpl.$SWITCH_TABLE$javax$json$JsonValue$ValueType = $switch_TABLE$javax$json$JsonValue$ValueType2;
    }
    
    private enum Scope
    {
        IN_NONE, 
        IN_OBJECT, 
        IN_ARRAY;
    }
    
    private static class Context
    {
        boolean first;
        final Scope scope;
        
        Context(final Scope scope) {
            super();
            this.first = true;
            this.scope = scope;
        }
    }
}
