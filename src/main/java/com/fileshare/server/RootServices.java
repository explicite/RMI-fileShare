package com.fileshare.server;

import com.fileshare.node.INode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

/**
 * @author Jan Paw
 *         Date: 5/28/13
 * @deprecated
 */
public class RootServices {
    private static LinkedList<INode> nodes;

    static class Server extends UnicastRemoteObject {
        protected Server(int port) throws RemoteException {
            super(port);
        }
    }
}
