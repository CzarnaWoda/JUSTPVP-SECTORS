package org.glassfish.json;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.json.JsonException;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParsingException;

import org.glassfish.json.api.BufferPool;

final class JsonTokenizer implements Closeable
{
    private static final int[] HEX;
    private static final int HEX_LENGTH;
    private final BufferPool bufferPool;
    private final Reader reader;
    private char[] buf;
    private int readBegin;
    private int readEnd;
    private int storeBegin;
    private int storeEnd;
    private long lineNo;
    private long lastLineOffset;
    private long bufferOffset;
    private boolean minus;
    private boolean fracOrExp;
    private BigDecimal bd;
    
    static {
        Arrays.fill(HEX = new int[128], -1);
        for (int i = 48; i <= 57; ++i) {
            JsonTokenizer.HEX[i] = i - 48;
        }
        for (int i = 65; i <= 70; ++i) {
            JsonTokenizer.HEX[i] = 10 + i - 65;
        }
        for (int i = 97; i <= 102; ++i) {
            JsonTokenizer.HEX[i] = 10 + i - 97;
        }
        HEX_LENGTH = JsonTokenizer.HEX.length;
    }
    
    JsonTokenizer(final Reader reader, final BufferPool bufferPool) {
        super();
        this.lineNo = 1L;
        this.lastLineOffset = 0L;
        this.bufferOffset = 0L;
        this.reader = reader;
        this.bufferPool = bufferPool;
        this.buf = bufferPool.take();
    }
    
    private void readString() {
        boolean inPlace = true;
        final int readBegin = this.readBegin;
        this.storeEnd = readBegin;
        this.storeBegin = readBegin;
        while (true) {
            if (inPlace) {
                int ch;
                while (this.readBegin < this.readEnd && (ch = this.buf[this.readBegin]) >= 32 && ch != 92) {
                    if (ch == 34) {
                        this.storeEnd = this.readBegin++;
                        return;
                    }
                    ++this.readBegin;
                }
                this.storeEnd = this.readBegin;
            }
            int ch = this.read();
            if (ch >= 32 && ch != 34 && ch != 92) {
                if (!inPlace) {
                    this.buf[this.storeEnd] = (char)ch;
                }
                ++this.storeEnd;
            }
            else {
                switch (ch) {
                    case 92: {
                        inPlace = false;
                        this.unescape();
                        continue;
                    }
                    case 34: {}
                    default: {
                        throw this.unexpectedChar(ch);
                    }
                }
            }
        }
    }
    
    private void unescape() {
        final int ch = this.read();
        switch (ch) {
            case 98: {
                this.buf[this.storeEnd++] = '\b';
                break;
            }
            case 116: {
                this.buf[this.storeEnd++] = '\t';
                break;
            }
            case 110: {
                this.buf[this.storeEnd++] = '\n';
                break;
            }
            case 102: {
                this.buf[this.storeEnd++] = '\f';
                break;
            }
            case 114: {
                this.buf[this.storeEnd++] = '\r';
                break;
            }
            case 34:
            case 47:
            case 92: {
                this.buf[this.storeEnd++] = (char)ch;
                break;
            }
            case 117: {
                int unicode = 0;
                for (int i = 0; i < 4; ++i) {
                    final int ch2 = this.read();
                    final int digit = (ch2 >= 0 && ch2 < JsonTokenizer.HEX_LENGTH) ? JsonTokenizer.HEX[ch2] : -1;
                    if (digit < 0) {
                        throw this.unexpectedChar(ch2);
                    }
                    unicode = (unicode << 4 | digit);
                }
                this.buf[this.storeEnd++] = (char)unicode;
                break;
            }
            default: {
                throw this.unexpectedChar(ch);
            }
        }
    }
    
    private int readNumberChar() {
        if (this.readBegin < this.readEnd) {
            return this.buf[this.readBegin++];
        }
        this.storeEnd = this.readBegin;
        return this.read();
    }
    
    private void readNumber(int ch) {
        final int n = this.readBegin - 1;
        this.storeEnd = n;
        this.storeBegin = n;
        if (ch == 45) {
            this.minus = true;
            ch = this.readNumberChar();
            if (ch < 48 || ch > 57) {
                throw this.unexpectedChar(ch);
            }
        }
        if (ch == 48) {
            ch = this.readNumberChar();
        }
        else {
            do {
                ch = this.readNumberChar();
            } while (ch >= 48 && ch <= 57);
        }
        if (ch == 46) {
            this.fracOrExp = true;
            int count = 0;
            do {
                ch = this.readNumberChar();
                ++count;
            } while (ch >= 48 && ch <= 57);
            if (count == 1) {
                throw this.unexpectedChar(ch);
            }
        }
        if (ch == 101 || ch == 69) {
            this.fracOrExp = true;
            ch = this.readNumberChar();
            if (ch == 43 || ch == 45) {
                ch = this.readNumberChar();
            }
            int count;
            for (count = 0; ch >= 48 && ch <= 57; ch = this.readNumberChar(), ++count) {}
            if (count == 0) {
                throw this.unexpectedChar(ch);
            }
        }
        --this.readBegin;
        this.storeEnd = this.readBegin;
    }
    
    private void readTrue() {
        final int ch1 = this.read();
        if (ch1 != 114) {
            throw this.expectedChar(ch1, 'r');
        }
        final int ch2 = this.read();
        if (ch2 != 117) {
            throw this.expectedChar(ch2, 'u');
        }
        final int ch3 = this.read();
        if (ch3 != 101) {
            throw this.expectedChar(ch3, 'e');
        }
    }
    
    private void readFalse() {
        final int ch1 = this.read();
        if (ch1 != 97) {
            throw this.expectedChar(ch1, 'a');
        }
        final int ch2 = this.read();
        if (ch2 != 108) {
            throw this.expectedChar(ch2, 'l');
        }
        final int ch3 = this.read();
        if (ch3 != 115) {
            throw this.expectedChar(ch3, 's');
        }
        final int ch4 = this.read();
        if (ch4 != 101) {
            throw this.expectedChar(ch4, 'e');
        }
    }
    
    private void readNull() {
        final int ch1 = this.read();
        if (ch1 != 117) {
            throw this.expectedChar(ch1, 'u');
        }
        final int ch2 = this.read();
        if (ch2 != 108) {
            throw this.expectedChar(ch2, 'l');
        }
        final int ch3 = this.read();
        if (ch3 != 108) {
            throw this.expectedChar(ch3, 'l');
        }
    }
    
    JsonToken nextToken() {
        this.reset();
        int ch = this.read();
        while (ch == 32 || ch == 9 || ch == 10 || ch == 13) {
            if (ch == 13) {
                ++this.lineNo;
                ch = this.read();
                if (ch != 10) {
                    this.lastLineOffset = this.bufferOffset + this.readBegin - 1L;
                    continue;
                }
                this.lastLineOffset = this.bufferOffset + this.readBegin;
            }
            else if (ch == 10) {
                ++this.lineNo;
                this.lastLineOffset = this.bufferOffset + this.readBegin;
            }
            ch = this.read();
        }
        switch (ch) {
            case 34: {
                this.readString();
                return JsonToken.STRING;
            }
            case 123: {
                return JsonToken.CURLYOPEN;
            }
            case 91: {
                return JsonToken.SQUAREOPEN;
            }
            case 58: {
                return JsonToken.COLON;
            }
            case 44: {
                return JsonToken.COMMA;
            }
            case 116: {
                this.readTrue();
                return JsonToken.TRUE;
            }
            case 102: {
                this.readFalse();
                return JsonToken.FALSE;
            }
            case 110: {
                this.readNull();
                return JsonToken.NULL;
            }
            case 93: {
                return JsonToken.SQUARECLOSE;
            }
            case 125: {
                return JsonToken.CURLYCLOSE;
            }
            case 45:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                this.readNumber(ch);
                return JsonToken.NUMBER;
            }
            case -1: {
                return JsonToken.EOF;
            }
            default: {
                throw this.unexpectedChar(ch);
            }
        }
    }
    
    JsonLocation getLastCharLocation() {
        return new JsonLocationImpl(this.lineNo, this.bufferOffset + this.readBegin - this.lastLineOffset, this.bufferOffset + this.readBegin - 1L);
    }
    
    JsonLocation getLocation() {
        return new JsonLocationImpl(this.lineNo, this.bufferOffset + this.readBegin - this.lastLineOffset + 1L, this.bufferOffset + this.readBegin);
    }
    
    private int read() {
        try {
            if (this.readBegin == this.readEnd) {
                final int len = this.fillBuf();
                if (len == -1) {
                    return -1;
                }
                assert len != 0;
                this.readBegin = this.storeEnd;
                this.readEnd = this.readBegin + len;
            }
            return this.buf[this.readBegin++];
        }
        catch (IOException ioe) {
            throw new JsonException(JsonMessages.TOKENIZER_IO_ERR(), ioe);
        }
    }
    
    private int fillBuf() throws IOException {
        if (this.storeEnd != 0) {
            final int storeLen = this.storeEnd - this.storeBegin;
            if (storeLen > 0) {
                if (storeLen == this.buf.length) {
                    final char[] doubleBuf = Arrays.copyOf(this.buf, 2 * this.buf.length);
                    this.bufferPool.recycle(this.buf);
                    this.buf = doubleBuf;
                }
                else {
                    System.arraycopy(this.buf, this.storeBegin, this.buf, 0, storeLen);
                    this.storeEnd = storeLen;
                    this.storeBegin = 0;
                    this.bufferOffset += this.readBegin - this.storeEnd;
                }
            }
            else {
                final boolean b = false;
                this.storeEnd = (b ? 1 : 0);
                this.storeBegin = (b ? 1 : 0);
                this.bufferOffset += this.readBegin;
            }
        }
        else {
            this.bufferOffset += this.readBegin;
        }
        return this.reader.read(this.buf, this.storeEnd, this.buf.length - this.storeEnd);
    }
    
    private void reset() {
        if (this.storeEnd != 0) {
            this.storeBegin = 0;
            this.storeEnd = 0;
            this.bd = null;
            this.minus = false;
            this.fracOrExp = false;
        }
    }
    
    String getValue() {
        return new String(this.buf, this.storeBegin, this.storeEnd - this.storeBegin);
    }
    
    BigDecimal getBigDecimal() {
        if (this.bd == null) {
            this.bd = new BigDecimal(this.buf, this.storeBegin, this.storeEnd - this.storeBegin);
        }
        return this.bd;
    }
    
    int getInt() {
        final int storeLen = this.storeEnd - this.storeBegin;
        if (!this.fracOrExp && (storeLen <= 9 || (this.minus && storeLen == 10))) {
            int num = 0;
            for (int i = this.minus ? 1 : 0; i < storeLen; ++i) {
                num = num * 10 + (this.buf[this.storeBegin + i] - '0');
            }
            return this.minus ? (-num) : num;
        }
        return this.getBigDecimal().intValue();
    }
    
    boolean isDefinitelyInt() {
        final int storeLen = this.storeEnd - this.storeBegin;
        return !this.fracOrExp && (storeLen <= 9 || (this.minus && storeLen == 10));
    }
    
    boolean isIntegral() {
        return !this.fracOrExp || this.getBigDecimal().scale() == 0;
    }
    
    @Override
    public void close() throws IOException {
        this.reader.close();
        this.bufferPool.recycle(this.buf);
    }
    
    private JsonParsingException unexpectedChar(final int ch) {
        final JsonLocation location = this.getLastCharLocation();
        return new JsonParsingException(JsonMessages.TOKENIZER_UNEXPECTED_CHAR(ch, location), location);
    }
    
    private JsonParsingException expectedChar(final int unexpected, final char expected) {
        final JsonLocation location = this.getLastCharLocation();
        return new JsonParsingException(JsonMessages.TOKENIZER_EXPECTED_CHAR(unexpected, location, expected), location);
    }
    
    enum JsonToken
    {
        CURLYOPEN("CURLYOPEN", 0, JsonParser.Event.START_OBJECT, false), 
        SQUAREOPEN("SQUAREOPEN", 1, JsonParser.Event.START_ARRAY, false), 
        COLON("COLON", 2, (JsonParser.Event)null, false), 
        COMMA("COMMA", 3, (JsonParser.Event)null, false), 
        STRING("STRING", 4, JsonParser.Event.VALUE_STRING, true), 
        NUMBER("NUMBER", 5, JsonParser.Event.VALUE_NUMBER, true), 
        TRUE("TRUE", 6, JsonParser.Event.VALUE_TRUE, true), 
        FALSE("FALSE", 7, JsonParser.Event.VALUE_FALSE, true), 
        NULL("NULL", 8, JsonParser.Event.VALUE_NULL, true), 
        CURLYCLOSE("CURLYCLOSE", 9, JsonParser.Event.END_OBJECT, false), 
        SQUARECLOSE("SQUARECLOSE", 10, JsonParser.Event.END_ARRAY, false), 
        EOF("EOF", 11, (JsonParser.Event)null, false);
        
        private final JsonParser.Event event;
        private final boolean value;
        
        private JsonToken(final String s, final int n, final JsonParser.Event event, final boolean value) {
            this.event = event;
            this.value = value;
        }
        
        JsonParser.Event getEvent() {
            return this.event;
        }
        
        boolean isValue() {
            return this.value;
        }
    }
}
