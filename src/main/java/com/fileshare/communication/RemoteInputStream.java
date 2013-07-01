package com.fileshare.communication;

import com.fileshare.file.io.Pipe;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public class RemoteInputStream implements InputStream, Serializable {
    private java.io.InputStream in;
    private byte[] bytes;

    public RemoteInputStream(java.io.InputStream in) throws IOException {
        this.in = in;
        UnicastRemoteObject.exportObject(this, 1099);
    }

    @Override
    public byte[] readBytes(int len) throws IOException, RemoteException {
        if (bytes == null || bytes.length != len)
            bytes = new byte[len];

        int len2 = in.read(bytes);
        if (len2 < 0)
            return null;

        if (len2 != len) {
            byte[] bytes2 = new byte[len2];
            System.arraycopy(bytes, 0, bytes2, 0, len2);
            return bytes2;
        } else
            return bytes;
    }

    @Override
    public int read() throws IOException, RemoteException {
        return in.read();
    }

    @Override
    public void close() throws IOException, RemoteException {
        in.close();
    }

    @Override
    public Pipe transfer(int key) throws IOException, RemoteException {
        return new Pipe(key, in);
    }
}
