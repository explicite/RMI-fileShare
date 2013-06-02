package com.fileshare.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
public interface IConnection extends Remote {
    public Message receive() throws RemoteException;

    public void send(Message message) throws RemoteException;
}
