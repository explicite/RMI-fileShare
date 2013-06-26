package com.fileshare.file.io;

import com.fileshare.communication.service.IInputStreamService;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public class InputStream extends java.io.InputStream implements Serializable {
    private IInputStreamService in;

    public InputStream(IInputStreamService in) {
        this.in = in;
    }

    public void transfer(java.io.OutputStream out) throws IOException {
        Pipe pipe = new Pipe(out);
        in.transfer(pipe.getKey());
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    public int read(byte[] bytes, int off, int len) throws IOException {
        byte[] stream = in.readBytes(len);
        if (stream == null)
            return -1;
        int i = stream.length;
        System.arraycopy(stream, 0, bytes, off, i);
        return i;
    }

    public void close() throws IOException {
        in.close();
        super.close();
    }
}
