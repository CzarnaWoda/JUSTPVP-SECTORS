package javax.json;

public interface JsonValue
{
    public static final JsonValue NULL = new JsonValue() {
        @Override
        public ValueType getValueType() {
            return ValueType.NULL;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof JsonValue && this.getValueType().equals(((JsonValue)obj).getValueType());
        }
        
        @Override
        public int hashCode() {
            return ValueType.NULL.hashCode();
        }
        
        @Override
        public String toString() {
            return "null";
        }
    };
    public static final JsonValue TRUE = new JsonValue() {
        @Override
        public ValueType getValueType() {
            return ValueType.TRUE;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof JsonValue && this.getValueType().equals(((JsonValue)obj).getValueType());
        }
        
        @Override
        public int hashCode() {
            return ValueType.TRUE.hashCode();
        }
        
        @Override
        public String toString() {
            return "true";
        }
    };
    public static final JsonValue FALSE = new JsonValue() {
        @Override
        public ValueType getValueType() {
            return ValueType.FALSE;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof JsonValue && this.getValueType().equals(((JsonValue)obj).getValueType());
        }
        
        @Override
        public int hashCode() {
            return ValueType.FALSE.hashCode();
        }
        
        @Override
        public String toString() {
            return "false";
        }
    };
    
    ValueType getValueType();
    
    String toString();
    
    public enum ValueType
    {
        ARRAY, 
        OBJECT, 
        STRING, 
        NUMBER, 
        TRUE, 
        FALSE, 
        NULL;
    }
}
