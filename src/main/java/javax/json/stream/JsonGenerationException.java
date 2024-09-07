package javax.json.stream;

import javax.json.JsonException;

@SuppressWarnings("serial")
public class JsonGenerationException extends JsonException
{
    public JsonGenerationException(final String message) {
        super(message);
    }
    
    public JsonGenerationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
