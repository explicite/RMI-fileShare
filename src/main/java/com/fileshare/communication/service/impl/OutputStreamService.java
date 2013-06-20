package com.fileshare.communication.service.impl;

import com.fileshare.communication.service.IOutputStreamService;
import com.fileshare.file.io.Pipe;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public class OutputStreamService implements IOutputStreamService {
    private java.io.OutputStream out;
    private Pipe pipe;

    public OutputStreamService(java.io.OutputStream out) throws RemoteException {
        this.out = out;
        this.pipe = new Pipe(out);
        UnicastRemoteObject.exportObject(this, 1099);
    }

    public int getPipeKey() {
        return pipe.getKey();
    }


    @Override
    public void write(int bytes) throws IOException, RemoteException {
        out.write(bytes);
    }

    @Override
    public void write(byte[] bytes, int off, int len) throws IOException, RemoteException {
        out.write(bytes, off, len);
    }

    @Override
    public void close() throws IOException, RemoteException {
        out.close();
        out.flush();
    }

    @Override
    public void transfer(Pipe pipe) throws IOException, RemoteException {
        //Serializable
    }
}
