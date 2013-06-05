package com.fileshare.communication;

import com.fileshare.communication.connection.Address;
import com.fileshare.communication.connection.IConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

/**
 * @author Jan Paw
 *         Date: 6/ 4/13
 */
public final class Client {
    private static final Logger logger = LogManager.getLogger(Client.class.getName());
    private static Address address;
    private IConnection connection = null;

    public static void main(String[] args) {
        System.setProperty("java.security.policy", Paths.get("").toAbsolutePath().toString()
                + "\\src\\main\\resources\\no.policy"); //TODO connection problems with client.policy

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
            logger.trace("Security manager installed.");
        } else {
            logger.trace("Security manager already exists.");
        }

        Client client = new Client();
        client.echo(address.toString());
    }

    private Integer getID() {
        try {
            return connection.getID();
        } catch (Exception e) {
            logger.error("Client exception: " + e);
            e.printStackTrace();
        }
        return 0;
    }

    private String echo(String s) {
        try {
            return connection.echo(s);
        } catch (Exception e) {
            logger.error("Client exception: " + e);
            e.printStackTrace();
        }
        return null;
    }

    public Client() {
        try {
            connect("//localhost/Server");
            address = new Address(getID());
        } catch (UnknownHostException e) {
            logger.error("Client exception: " + e);
            e.printStackTrace();
        } catch (RemoteException e) {
            logger.error("Client exception: " + e);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            logger.error("Client exception: " + e);
            e.printStackTrace();
        } catch (NotBoundException e) {
            logger.error("Client exception: " + e);
            e.printStackTrace();
        }
    }

    public void connect(String s) throws RemoteException, NotBoundException, MalformedURLException {
        connection = (IConnection) Naming.lookup(s);
    }
}
