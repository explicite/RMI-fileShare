package com.fileshare.communication;

import com.fileshare.file.Packet;
import com.fileshare.network.Address;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Jan Paw
 *         Date: 6/30/13
 */
public interface Peer extends Remote {
    public java.io.OutputStream getOutputStream(File f)
            throws IOException, RemoteException;

    public java.io.InputStream getInputStream(File f)
            throws IOException, RemoteException;

    public void delete(File f)
            throws IOException, RemoteException;

    public boolean fusion(Address address)
            throws IOException, RemoteException;

    @Deprecated
    public void receive(Packet packet)
            throws RemoteException;
}
