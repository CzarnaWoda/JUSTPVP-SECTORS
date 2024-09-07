package org.glassfish.json;

import javax.json.stream.JsonLocation;

class JsonLocationImpl implements JsonLocation
{
    static final JsonLocation UNKNOWN;
    private final long columnNo;
    private final long lineNo;
    private final long offset;
    
    static {
        UNKNOWN = new JsonLocationImpl(-1L, -1L, -1L);
    }
    
    JsonLocationImpl(final long lineNo, final long columnNo, final long streamOffset) {
        super();
        this.lineNo = lineNo;
        this.columnNo = columnNo;
        this.offset = streamOffset;
    }
    
    @Override
    public long getLineNumber() {
        return this.lineNo;
    }
    
    @Override
    public long getColumnNumber() {
        return this.columnNo;
    }
    
    @Override
    public long getStreamOffset() {
        return this.offset;
    }
    
    @Override
    public String toString() {
        return "(line no=" + this.lineNo + ", column no=" + this.columnNo + ", offset=" + this.offset + ")";
    }
}
