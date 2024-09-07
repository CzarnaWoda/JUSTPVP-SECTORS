package org.glassfish.json;

import javax.json.JsonString;
import javax.json.JsonValue;

final class JsonStringImpl implements JsonString
{
    private final String value;
    
    JsonStringImpl(final String value) {
        super();
        this.value = value;
    }
    
    @Override
    public String getString() {
        return this.value;
    }
    
    @Override
    public CharSequence getChars() {
        return this.value;
    }
    
    @Override
    public ValueType getValueType() {
        return ValueType.STRING;
    }
    
    @Override
    public int hashCode() {
        return this.getString().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JsonString)) {
            return false;
        }
        final JsonString other = (JsonString)obj;
        return this.getString().equals(other.getString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('\"');
        for (int i = 0; i < this.value.length(); ++i) {
            final char c = this.value.charAt(i);
            if (c >= ' ' && c <= 1114111 && c != '\"' && c != '\\') {
                sb.append(c);
            }
            else {
                switch (c) {
                    case '\"':
                    case '\\': {
                        sb.append('\\');
                        sb.append(c);
                        break;
                    }
                    case '\b': {
                        sb.append('\\');
                        sb.append('b');
                        break;
                    }
                    case '\f': {
                        sb.append('\\');
                        sb.append('f');
                        break;
                    }
                    case '\n': {
                        sb.append('\\');
                        sb.append('n');
                        break;
                    }
                    case '\r': {
                        sb.append('\\');
                        sb.append('r');
                        break;
                    }
                    case '\t': {
                        sb.append('\\');
                        sb.append('t');
                        break;
                    }
                    default: {
                        final String hex = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(hex.substring(hex.length() - 4));
                        break;
                    }
                }
            }
        }
        sb.append('\"');
        return sb.toString();
    }
}
