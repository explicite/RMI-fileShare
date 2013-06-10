package com.fileshare.communication.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Jan Paw
 *         Date: 6/7/13
 */
public interface INodeService extends Remote {
    public String echo(String s) throws RemoteException;

    public void bind(INodeService connection) throws RemoteException;
}
