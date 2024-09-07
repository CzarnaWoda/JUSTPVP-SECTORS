package javax.json;

@SuppressWarnings("serial")
public class JsonException extends RuntimeException
{
    public JsonException(final String message) {
        super(message);
    }
    
    public JsonException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
