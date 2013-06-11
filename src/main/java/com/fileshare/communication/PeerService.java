package com.fileshare.communication;

import com.fileshare.communication.service.impl.InputStreamService;
import com.fileshare.communication.service.impl.OutputStreamService;
import com.fileshare.file.io.InputStream;
import com.fileshare.file.io.OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public class PeerService {
    private static final Logger logger = LogManager.getLogger(PeerService.class.getName());

    public interface IPeer extends Remote {
        public java.io.OutputStream getOutputStream(File f)
                throws IOException, RemoteException;

        public java.io.InputStream getInputStream(File f)
                throws IOException, RemoteException;
    }

    public static class Peer extends UnicastRemoteObject implements IPeer {
        Registry registry;
        String name;

        public Peer(String name) throws RemoteException {
            super();
            this.name = name;
            logger.info("New peer: " + name);
        }

        public void start() throws Exception {
            registry = LocateRegistry.createRegistry(1099);
            registry.bind(name, this);
            logger.info("Binding peer: " + name);
        }

        public void stop() throws Exception {
            registry.unbind(name);
            unexportObject(this, true);
            unexportObject(registry, true);
            logger.info("Unbinding peer: " + name);
        }

        @Override
        public java.io.OutputStream getOutputStream(File f) throws IOException, RemoteException {
            return new OutputStream(new OutputStreamService(new FileOutputStream(f)));
        }

        @Override
        public java.io.InputStream getInputStream(File f) throws IOException, RemoteException {
            return new InputStream(new InputStreamService(new FileInputStream(f)));
        }
    }

    public static void main(String[] args) throws Exception {
        String name;
        if (args.length > 0)
            name = args[0];
        else
            name = "peer";

        Peer peer = new Peer(name);
        peer.start();
        Thread.sleep(5 * 60 * 1000); //TODO remove when PeerService become server
        peer.stop();
    }
}
