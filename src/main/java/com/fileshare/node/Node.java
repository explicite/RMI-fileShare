package com.fileshare.node;

import com.fileshare.communication.Address;
import com.fileshare.communication.IConnection;
import com.fileshare.communication.Message;
import com.fileshare.file.RemoteFile;
import com.fileshare.time.Clock;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
public class Node extends UnicastRemoteObject implements IConnection {
    private final Address address;
    private Clock clock;
    private LinkedList<Connection> connections;
    private LinkedList<RemoteFile> files;

    protected Node() throws UnknownHostException, RemoteException {
        super();
        this.address = new Address();
    }

    @Override
    public Message receive() {
        return null;  //TODO + RemoteException
    }

    @Override
    public void send(Message message) {
        //TODO + RemoteException
    }

    public class Connection implements IConnection, Runnable {
        private Queue<Message> buffer;

        @Override
        public Message receive() throws RemoteException {
            return null;  //TODO
        }

        @Override
        public void send(Message message) throws RemoteException {
            //TODO
        }

        @Override
        public void run() {
            //TODO petla do sprawdzania buffer - fork/join z jdk7
        }
    }
}
