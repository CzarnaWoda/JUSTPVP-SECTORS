package org.glassfish.json.api;

public interface BufferPool
{
    char[] take();
    
    void recycle(char[] p0);
}
