package org.glassfish.json;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.json.JsonException;

class UnicodeDetectingInputStream extends FilterInputStream
{
    private static final Charset UTF_8;
    private static final Charset UTF_16BE;
    private static final Charset UTF_16LE;
    private static final Charset UTF_32LE;
    private static final Charset UTF_32BE;
    private final byte[] buf;
    private int bufLen;
    private int curIndex;
    private final Charset charset;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
        UTF_16BE = Charset.forName("UTF-16BE");
        UTF_16LE = Charset.forName("UTF-16LE");
        UTF_32LE = Charset.forName("UTF-32LE");
        UTF_32BE = Charset.forName("UTF-32BE");
    }
    
    UnicodeDetectingInputStream(final InputStream is) {
        super(is);
        this.buf = new byte[4];
        this.charset = this.detectEncoding();
    }
    
    Charset getCharset() {
        return this.charset;
    }
    
    private void fillBuf() {
        try {
            final int b1 = this.in.read();
            if (b1 == -1) {
                return;
            }
            final int b2 = this.in.read();
            if (b2 == -1) {
                this.bufLen = 1;
                this.buf[0] = (byte)b1;
                return;
            }
            final int b3 = this.in.read();
            if (b3 == -1) {
                this.bufLen = 2;
                this.buf[0] = (byte)b1;
                this.buf[1] = (byte)b2;
                return;
            }
            final int b4 = this.in.read();
            if (b4 == -1) {
                this.bufLen = 3;
                this.buf[0] = (byte)b1;
                this.buf[1] = (byte)b2;
                this.buf[2] = (byte)b3;
                return;
            }
            this.bufLen = 4;
            this.buf[0] = (byte)b1;
            this.buf[1] = (byte)b2;
            this.buf[2] = (byte)b3;
            this.buf[3] = (byte)b4;
        }
        catch (IOException ioe) {
            throw new JsonException("I/O error while auto-detecting the encoding of stream", ioe);
        }
    }
    
    private Charset detectEncoding() {
        this.fillBuf();
        if (this.bufLen < 2) {
            throw new JsonException("Cannot auto-detect encoding, not enough chars");
        }
        if (this.bufLen == 4) {
            if (this.buf[0] == 0 && this.buf[1] == 0 && this.buf[2] == -2 && this.buf[3] == -1) {
                this.curIndex = 4;
                return UnicodeDetectingInputStream.UTF_32BE;
            }
            if (this.buf[0] == -1 && this.buf[1] == -2 && this.buf[2] == 0 && this.buf[3] == 0) {
                this.curIndex = 4;
                return UnicodeDetectingInputStream.UTF_32LE;
            }
            if (this.buf[0] == -2 && this.buf[1] == -1) {
                this.curIndex = 2;
                return UnicodeDetectingInputStream.UTF_16BE;
            }
            if (this.buf[0] == -1 && this.buf[1] == -2) {
                this.curIndex = 2;
                return UnicodeDetectingInputStream.UTF_16LE;
            }
            if (this.buf[0] == -17 && this.buf[1] == -69 && this.buf[2] == -65) {
                this.curIndex = 3;
                return UnicodeDetectingInputStream.UTF_8;
            }
            if (this.buf[0] == 0 && this.buf[1] == 0 && this.buf[2] == 0) {
                return UnicodeDetectingInputStream.UTF_32BE;
            }
            if (this.buf[0] == 0 && this.buf[2] == 0) {
                return UnicodeDetectingInputStream.UTF_16BE;
            }
            if (this.buf[1] == 0 && this.buf[2] == 0 && this.buf[3] == 0) {
                return UnicodeDetectingInputStream.UTF_32LE;
            }
            if (this.buf[1] == 0 && this.buf[3] == 0) {
                return UnicodeDetectingInputStream.UTF_16LE;
            }
        }
        return UnicodeDetectingInputStream.UTF_8;
    }
    
    @Override
    public int read() throws IOException {
        if (this.curIndex < this.bufLen) {
            return this.buf[this.curIndex++];
        }
        return this.in.read();
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.curIndex >= this.bufLen) {
            return this.in.read(b, off, len);
        }
        if (len == 0) {
            return 0;
        }
        if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        }
        final int min = Math.min(this.bufLen - this.curIndex, len);
        System.arraycopy(this.buf, this.curIndex, b, off, min);
        this.curIndex += min;
        return min;
    }
}
