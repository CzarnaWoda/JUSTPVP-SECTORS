package org.glassfish.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.json.JsonException;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParsingException;

import org.glassfish.json.api.BufferPool;


public class JsonParserImpl
  implements JsonParser
{
  private Context currentContext = new NoneContext();
  private Event currentEvent;
  private final Stack stack = new Stack();
  private final StateIterator stateIterator;
  private final JsonTokenizer tokenizer;
  
  public JsonParserImpl(Reader reader, BufferPool bufferPool)
  {
    this.tokenizer = new JsonTokenizer(reader, bufferPool);
    this.stateIterator = new StateIterator();
  }
  
  public JsonParserImpl(InputStream in, BufferPool bufferPool)
  {
    UnicodeDetectingInputStream uin = new UnicodeDetectingInputStream(in);
    this.tokenizer = new JsonTokenizer(new InputStreamReader(uin, uin.getCharset()), bufferPool);
    this.stateIterator = new StateIterator();
  }
  
  public JsonParserImpl(InputStream in, Charset encoding, BufferPool bufferPool)
  {
    this.tokenizer = new JsonTokenizer(new InputStreamReader(in, encoding), bufferPool);
    this.stateIterator = new StateIterator();
  }
    
    @Override
    public String getString() {
        if (this.currentEvent == Event.KEY_NAME || this.currentEvent == Event.VALUE_STRING || this.currentEvent == Event.VALUE_NUMBER) {
            return this.tokenizer.getValue();
        }
        throw new IllegalStateException(JsonMessages.PARSER_GETSTRING_ERR(this.currentEvent));
    }
    
    @Override
    public boolean isIntegralNumber() {
        if (this.currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException(JsonMessages.PARSER_ISINTEGRALNUMBER_ERR(this.currentEvent));
        }
        return this.tokenizer.isIntegral();
    }
    
    @Override
    public int getInt() {
        if (this.currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException(JsonMessages.PARSER_GETINT_ERR(this.currentEvent));
        }
        return this.tokenizer.getInt();
    }
    
    boolean isDefinitelyInt() {
        return this.tokenizer.isDefinitelyInt();
    }
    
    @Override
    public long getLong() {
        if (this.currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException(JsonMessages.PARSER_GETLONG_ERR(this.currentEvent));
        }
        return this.tokenizer.getBigDecimal().longValue();
    }
    
    @Override
    public BigDecimal getBigDecimal() {
        if (this.currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException(JsonMessages.PARSER_GETBIGDECIMAL_ERR(this.currentEvent));
        }
        return this.tokenizer.getBigDecimal();
    }
    
    @Override
    public JsonLocation getLocation() {
        return this.tokenizer.getLocation();
    }
    
    public JsonLocation getLastCharLocation() {
        return this.tokenizer.getLastCharLocation();
    }
    
    @Override
    public boolean hasNext() {
        return this.stateIterator.hasNext();
    }
    
    @Override
    public Event next() {
        return this.stateIterator.next();
    }
    
    @Override
    public void close() {
        try {
            this.tokenizer.close();
        }
        catch (IOException e) {
            throw new JsonException(JsonMessages.PARSER_TOKENIZER_CLOSE_IO(), e);
        }
    }
    
    private JsonParsingException parsingException(final JsonTokenizer.JsonToken token, final String expectedTokens) {
        final JsonLocation location = this.getLastCharLocation();
        return new JsonParsingException(JsonMessages.PARSER_INVALID_TOKEN(token, location, expectedTokens), location);
    }
    
    static /* synthetic */ void access$4(final JsonParserImpl jsonParserImpl, final Event currentEvent) {
        jsonParserImpl.currentEvent = currentEvent;
    }
    
    static /* synthetic */ void access$5(final JsonParserImpl jsonParserImpl, final Context currentContext) {
        jsonParserImpl.currentContext = currentContext;
    }
    
    private class StateIterator implements Iterator<Event>
    {
        @Override
        public boolean hasNext() {
            if (!JsonParserImpl.this.stack.isEmpty() || (JsonParserImpl.this.currentEvent != Event.END_ARRAY && JsonParserImpl.this.currentEvent != Event.END_OBJECT)) {
                return true;
            }
            final JsonTokenizer.JsonToken token = JsonParserImpl.this.tokenizer.nextToken();
            if (token != JsonTokenizer.JsonToken.EOF) {
                throw new JsonParsingException(JsonMessages.PARSER_EXPECTED_EOF(token), JsonParserImpl.this.getLastCharLocation());
            }
            return false;
        }
        
        @Override
        public Event next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final JsonParserImpl this$0 = JsonParserImpl.this;
            final Event nextEvent = JsonParserImpl.this.currentContext.getNextEvent();
            JsonParserImpl.access$4(this$0, nextEvent);
            return nextEvent;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private static final class Stack
    {
        private Context head;
        
        private void push(final Context context) {
            context.next = this.head;
            this.head = context;
        }
        
        private Context pop() {
            if (this.head == null) {
                throw new NoSuchElementException();
            }
            final Context temp = this.head;
            this.head = this.head.next;
            return temp;
        }
        
        private boolean isEmpty() {
            return this.head == null;
        }
    }
    
    private abstract class Context
    {
        Context next;
        
        abstract Event getNextEvent();
    }
    
    private final class NoneContext extends Context
    {
        private NoneContext() {
            super();
        }
        
        public Event getNextEvent() {
            final JsonTokenizer.JsonToken token = JsonParserImpl.this.tokenizer.nextToken();
            if (token == JsonTokenizer.JsonToken.CURLYOPEN) {
                JsonParserImpl.this.stack.push(JsonParserImpl.this.currentContext);
                JsonParserImpl.access$5(JsonParserImpl.this, new ObjectContext());
                return Event.START_OBJECT;
            }
            if (token == JsonTokenizer.JsonToken.SQUAREOPEN) {
                JsonParserImpl.this.stack.push(JsonParserImpl.this.currentContext);
                JsonParserImpl.access$5(JsonParserImpl.this, new ArrayContext());
                return Event.START_ARRAY;
            }
            throw JsonParserImpl.this.parsingException(token, "[CURLYOPEN, SQUAREOPEN]");
        }
    }
    
    private final class ObjectContext extends Context
    {
        private boolean firstValue;
        
        private ObjectContext() {
            super();
            this.firstValue = true;
        }
        
        public Event getNextEvent() {
            JsonTokenizer.JsonToken token = JsonParserImpl.this.tokenizer.nextToken();
            if (JsonParserImpl.this.currentEvent == Event.KEY_NAME) {
                if (token != JsonTokenizer.JsonToken.COLON) {
                    throw JsonParserImpl.this.parsingException(token, "[COLON]");
                }
                token = JsonParserImpl.this.tokenizer.nextToken();
                if (token.isValue()) {
                    return token.getEvent();
                }
                if (token == JsonTokenizer.JsonToken.CURLYOPEN) {
                    JsonParserImpl.this.stack.push(JsonParserImpl.this.currentContext);
                    JsonParserImpl.access$5(JsonParserImpl.this, new ObjectContext());
                    return Event.START_OBJECT;
                }
                if (token == JsonTokenizer.JsonToken.SQUAREOPEN) {
                    JsonParserImpl.this.stack.push(JsonParserImpl.this.currentContext);
                    JsonParserImpl.access$5(JsonParserImpl.this, new ArrayContext());
                    return Event.START_ARRAY;
                }
                throw JsonParserImpl.this.parsingException(token, "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]");
            }
            else {
                if (token == JsonTokenizer.JsonToken.CURLYCLOSE) {
                    JsonParserImpl.access$5(JsonParserImpl.this, JsonParserImpl.this.stack.pop());
                    return Event.END_OBJECT;
                }
                if (this.firstValue) {
                    this.firstValue = false;
                }
                else {
                    if (token != JsonTokenizer.JsonToken.COMMA) {
                        throw JsonParserImpl.this.parsingException(token, "[COMMA]");
                    }
                    token = JsonParserImpl.this.tokenizer.nextToken();
                }
                if (token == JsonTokenizer.JsonToken.STRING) {
                    return Event.KEY_NAME;
                }
                throw JsonParserImpl.this.parsingException(token, "[STRING]");
            }
        }
    }
    
    private final class ArrayContext extends Context
    {
        private boolean firstValue;
        
        private ArrayContext() {
            super();
            this.firstValue = true;
        }
        
        public Event getNextEvent() {
            JsonTokenizer.JsonToken token = JsonParserImpl.this.tokenizer.nextToken();
            if (token == JsonTokenizer.JsonToken.SQUARECLOSE) {
                JsonParserImpl.access$5(JsonParserImpl.this, JsonParserImpl.this.stack.pop());
                return Event.END_ARRAY;
            }
            if (this.firstValue) {
                this.firstValue = false;
            }
            else {
                if (token != JsonTokenizer.JsonToken.COMMA) {
                    throw JsonParserImpl.this.parsingException(token, "[COMMA]");
                }
                token = JsonParserImpl.this.tokenizer.nextToken();
            }
            if (token.isValue()) {
                return token.getEvent();
            }
            if (token == JsonTokenizer.JsonToken.CURLYOPEN) {
                JsonParserImpl.this.stack.push(JsonParserImpl.this.currentContext);
                JsonParserImpl.access$5(JsonParserImpl.this, new ObjectContext());
                return Event.START_OBJECT;
            }
            if (token == JsonTokenizer.JsonToken.SQUAREOPEN) {
                JsonParserImpl.this.stack.push(JsonParserImpl.this.currentContext);
                JsonParserImpl.access$5(JsonParserImpl.this, new ArrayContext());
                return Event.START_ARRAY;
            }
            throw JsonParserImpl.this.parsingException(token, "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]");
        }
    }
}
