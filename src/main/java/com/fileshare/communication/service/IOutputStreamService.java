package com.fileshare.communication.service;

import com.fileshare.file.io.Pipe;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public interface IOutputStreamService extends Remote {
    public void write(int bytes)
            throws IOException, RemoteException;

    public void write(byte[] bytes, int off, int len)
            throws IOException, RemoteException;

    public void close()
            throws IOException, RemoteException;

    void transfer(Pipe pipe)
            throws IOException, RemoteException;
}
