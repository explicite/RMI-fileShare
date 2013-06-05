package com.fileshare.communication;

import com.fileshare.communication.connection.Address;
import com.fileshare.communication.connection.IConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Jan Paw
 *         Date: 6/4/13
 */

public final class Server extends UnicastRemoteObject implements IConnection {
    private static final Logger logger = LogManager.getLogger(Server.class.getName());
    private static volatile Integer nodeID = 0;
    private static Address address;

    static {
        try {
            address = new Address(nodeID);
        } catch (UnknownHostException e) {
            logger.error("Unknown host:" + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.setProperty("java.security.policy", Paths.get("").toAbsolutePath().toString()
                + "\\src\\main\\resources\\no" +
                ".policy");     //TODO connection problems with server.policy

        logger.trace("server run | address=" + address);

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
            logger.trace("Security manager installed.");
        } else {
            logger.trace("Security manager already exists.");
        }

        try {
            LocateRegistry.createRegistry(1099);
            logger.trace("java RMI registry created.");
        } catch (RemoteException e) {
            logger.trace("java RMI registry already exists.");
        }

        try {
            Server server = new Server();
            Naming.rebind("//localhost/Server", server);
            logger.trace("PeerServer bound in registry");
        } catch (Exception e) {
            logger.error("RMI server exception:" + e);
            e.printStackTrace();
        }
    }

    protected Server() throws RemoteException {
        //TODO
    }

    @Override
    public Integer getID() throws RemoteException {
        logger.trace("nodeID=" + (nodeID + 1));
        return nodeID++;
    }

    @Override
    public String echo(String s) throws RemoteException {
        logger.trace("echo method: " + s);
        return "echo method: " + s;
    }
}
