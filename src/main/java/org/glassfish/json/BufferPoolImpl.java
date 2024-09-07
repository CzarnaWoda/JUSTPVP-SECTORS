package org.glassfish.json;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.glassfish.json.api.BufferPool;

class BufferPoolImpl implements BufferPool
{
    private volatile WeakReference<ConcurrentLinkedQueue<char[]>> queue;
    
    @Override
    public final char[] take() {
        final char[] t = this.getQueue().poll();
        if (t == null) {
            return new char[4096];
        }
        return t;
    }
    
    private ConcurrentLinkedQueue<char[]> getQueue() {
        final WeakReference<ConcurrentLinkedQueue<char[]>> q = this.queue;
        if (q != null) {
            final ConcurrentLinkedQueue<char[]> d = q.get();
            if (d != null) {
                return d;
            }
        }
        final ConcurrentLinkedQueue<char[]> d = new ConcurrentLinkedQueue<char[]>();
        this.queue = new WeakReference<ConcurrentLinkedQueue<char[]>>(d);
        return d;
    }
    
    @Override
    public final void recycle(final char[] t) {
        this.getQueue().offer(t);
    }
}
