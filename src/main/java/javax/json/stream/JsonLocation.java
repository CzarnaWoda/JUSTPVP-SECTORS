package javax.json.stream;

public interface JsonLocation
{
    long getLineNumber();
    
    long getColumnNumber();
    
    long getStreamOffset();
}
