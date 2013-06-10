package com.fileshare.communication;

import com.fileshare.communication.connection.INodeService;
import com.fileshare.configuration.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

/**
 * @author Jan Paw
 *         Date: 6/7/13
 */
public class Node {
    private static final Logger logger = LogManager.getLogger(Node.class.getName());
    private String name = null;
    private LinkedList<INodeService> connections = new LinkedList<>();

    public Node(String name) {
        super();
        this.name = name;
        bind();
    }

    static class NodeService extends UnicastRemoteObject implements INodeService {
        protected NodeService() throws RemoteException {
            super();
            logger.info("New connection in Node: ");
        }

        @Override
        public String echo(String s) throws RemoteException {
            logger.trace("New echo call: " + s);
            return s;
        }

        @Override
        public void bind(INodeService connection) throws RemoteException {
            //TODO
        }
    }

    private void bind() {
        try {
            Naming.rebind(name, new NodeService());
            logger.trace(name + " bound in registry");
        } catch (Exception e) {
            logger.error("Node server side exception:" + e);
            e.printStackTrace();
        }
    }

    //TODO for tests only - should be excluded from Node.class!

    public void connect(String s) {
        try {
            INodeService connection = (INodeService) Naming.lookup(s);
            connections.add(connection);
            logger.trace("New connection");
        } catch (NotBoundException e) {
            logger.entry(e);
        } catch (MalformedURLException | RemoteException e) {
            logger.error(e);
        }
    }

    public String echo(String s) {
        String echo = Messages.ERROR;
        try {
            echo = connections.getFirst().echo(s);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return echo;
    }
}
