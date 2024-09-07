package org.glassfish.json;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.JsonNumber;
import javax.json.JsonValue;

abstract class JsonNumberImpl implements JsonNumber
{
    static JsonNumber getJsonNumber(final int num) {
        return new JsonIntNumber(num);
    }
    
    static JsonNumber getJsonNumber(final long num) {
        return new JsonLongNumber(num);
    }
    
    static JsonNumber getJsonNumber(final BigInteger value) {
        return new JsonBigDecimalNumber(new BigDecimal(value));
    }
    
    static JsonNumber getJsonNumber(final double value) {
        return new JsonBigDecimalNumber(BigDecimal.valueOf(value));
    }
    
    static JsonNumber getJsonNumber(final BigDecimal value) {
        return new JsonBigDecimalNumber(value);
    }
    
    @Override
    public boolean isIntegral() {
        return this.bigDecimalValue().scale() == 0;
    }
    
    @Override
    public int intValue() {
        return this.bigDecimalValue().intValue();
    }
    
    @Override
    public int intValueExact() {
        return this.bigDecimalValue().intValueExact();
    }
    
    @Override
    public long longValue() {
        return this.bigDecimalValue().longValue();
    }
    
    @Override
    public long longValueExact() {
        return this.bigDecimalValue().longValueExact();
    }
    
    @Override
    public double doubleValue() {
        return this.bigDecimalValue().doubleValue();
    }
    
    @Override
    public BigInteger bigIntegerValue() {
        return this.bigDecimalValue().toBigInteger();
    }
    
    @Override
    public BigInteger bigIntegerValueExact() {
        return this.bigDecimalValue().toBigIntegerExact();
    }
    
    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }
    
    @Override
    public int hashCode() {
        return this.bigDecimalValue().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JsonNumber)) {
            return false;
        }
        final JsonNumber other = (JsonNumber)obj;
        return this.bigDecimalValue().equals(other.bigDecimalValue());
    }
    
    @Override
    public String toString() {
        return this.bigDecimalValue().toString();
    }
    
    private static final class JsonIntNumber extends JsonNumberImpl
    {
        private final int num;
        private BigDecimal bigDecimal;
        
        JsonIntNumber(final int num) {
            super();
            this.num = num;
        }
        
        @Override
        public boolean isIntegral() {
            return true;
        }
        
        @Override
        public int intValue() {
            return this.num;
        }
        
        @Override
        public int intValueExact() {
            return this.num;
        }
        
        @Override
        public long longValue() {
            return this.num;
        }
        
        @Override
        public long longValueExact() {
            return this.num;
        }
        
        @Override
        public double doubleValue() {
            return this.num;
        }
        
        @Override
        public BigDecimal bigDecimalValue() {
            BigDecimal bd = this.bigDecimal;
            if (bd == null) {
                bd = (this.bigDecimal = new BigDecimal(this.num));
            }
            return bd;
        }
        
        @Override
        public String toString() {
            return Integer.toString(this.num);
        }
    }
    
    private static final class JsonLongNumber extends JsonNumberImpl
    {
        private final long num;
        private BigDecimal bigDecimal;
        
        JsonLongNumber(final long num) {
            super();
            this.num = num;
        }
        
        @Override
        public boolean isIntegral() {
            return true;
        }
        
        @Override
        public long longValue() {
            return this.num;
        }
        
        @Override
        public long longValueExact() {
            return this.num;
        }
        
        @Override
        public double doubleValue() {
            return this.num;
        }
        
        @Override
        public BigDecimal bigDecimalValue() {
            BigDecimal bd = this.bigDecimal;
            if (bd == null) {
                bd = (this.bigDecimal = new BigDecimal(this.num));
            }
            return bd;
        }
        
        @Override
        public String toString() {
            return Long.toString(this.num);
        }
    }
    
    private static final class JsonBigDecimalNumber extends JsonNumberImpl
    {
        private final BigDecimal bigDecimal;
        
        JsonBigDecimalNumber(final BigDecimal value) {
            super();
            this.bigDecimal = value;
        }
        
        @Override
        public BigDecimal bigDecimalValue() {
            return this.bigDecimal;
        }
    }
}
