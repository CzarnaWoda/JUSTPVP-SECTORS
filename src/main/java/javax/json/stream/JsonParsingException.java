package javax.json.stream;

import javax.json.JsonException;
@SuppressWarnings("serial")
public class JsonParsingException extends JsonException
{
    private final JsonLocation location;
    
    public JsonParsingException(final String message, final JsonLocation location) {
        super(message);
        this.location = location;
    }
    
    public JsonParsingException(final String message, final Throwable cause, final JsonLocation location) {
        super(message, cause);
        this.location = location;
    }
    
    public JsonLocation getLocation() {
        return this.location;
    }
}
