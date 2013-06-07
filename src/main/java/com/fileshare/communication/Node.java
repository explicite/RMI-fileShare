package com.fileshare.communication;

import com.fileshare.communication.connection.INodeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

/**
 * @author Jan Paw
 *         Date: 6/7/13
 */
public class Node {
    private static final Logger logger = LogManager.getLogger(Node.class.getName());
    private static LinkedList<INodeService> connections = new LinkedList<INodeService>();
    //TODO kontener adresow

    protected Node() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {
        setPolicy();
        setSecurityManager();
        setRegistry();
        bindName(getString());
        Node node = new Node();
        node.connect(getString());
        logger.info(connections.getFirst().echo(getString()));

    }

    static class NodeService extends UnicastRemoteObject implements INodeService {
        protected NodeService() throws RemoteException {
            super();
            logger.info("New connection in Node: ");
        }

        @Override
        public String echo(String s) throws RemoteException {
            logger.trace("new echo call: " + s);
            return s;
        }

        @Override
        public void bind(INodeService connection) throws RemoteException {

        }
    }

    private static void setPolicy() {
        System.setProperty("java.security.policy", Paths.get("").toAbsolutePath().toString()
                + "\\src\\main\\resources\\no.policy");
    }

    private static void setSecurityManager() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
            logger.trace("Security manager installed.");
        } else {
            logger.trace("Security manager already exists.");
        }
    }

    private static void setRegistry() {
        try {
            LocateRegistry.createRegistry(1099);
            logger.trace("java RMI registry created.");
        } catch (RemoteException e) {
            logger.trace("java RMI registry already exists.");
        }
    }

    private static void bindName(String name) {
        try {
            Naming.rebind(name, new NodeService());
            logger.trace(name + " bound in registry");
        } catch (Exception e) {
            logger.error("Node server side exception:" + e);
            e.printStackTrace();
        }
    }


    //TODO for tests!
    public static String getString() {
        InputStreamReader inp = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(inp);

        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void connect(String s) {
        try {
            INodeService connection = (INodeService) Naming.lookup(s);
            connections.add(connection);
            logger.trace("new connection");
        } catch (NotBoundException e) {
            logger.entry(e);
        } catch (MalformedURLException e) {
            logger.error(e);
        } catch (RemoteException e) {
            logger.error(e);
        }
    }
}
