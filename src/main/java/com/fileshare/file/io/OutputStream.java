package com.fileshare.file.io;

import com.fileshare.communication.RemoteOutputStream;

import java.io.IOException;
import java.io.Serializable;


/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public class OutputStream extends java.io.OutputStream implements Serializable {
    private com.fileshare.communication.OutputStream out;
    private int pipeKey;

    public OutputStream(RemoteOutputStream out) {
        this.out = out;
    }


    public void transfer(java.io.InputStream in) throws IOException {
        Pipe pipe = new Pipe(pipeKey, in);
        out.transfer(pipe);
    }

    @Override
    public void write(int bytes) throws IOException {
        out.write(bytes);
    }

    public void write(byte[] bytes, int off, int len) throws IOException {
        out.write(bytes, off, len);
    }

    public void close() throws IOException {
        out.close();
    }
}
