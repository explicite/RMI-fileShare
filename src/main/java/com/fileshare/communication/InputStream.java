package com.fileshare.communication;

import com.fileshare.file.io.Pipe;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public interface InputStream extends Remote {
    public byte[] readBytes(int len)
            throws IOException, RemoteException;

    public int read()
            throws IOException, RemoteException;

    public void close()
            throws IOException, RemoteException;

    Pipe transfer(int key)
            throws IOException, RemoteException;
}
