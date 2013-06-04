package com.fileshare.communication.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
public interface IConnection extends Remote {
    public Integer getID() throws RemoteException;
    public String echo(String s) throws RemoteException;
}
