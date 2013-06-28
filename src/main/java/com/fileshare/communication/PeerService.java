package com.fileshare.communication;

import com.fileshare.communication.service.impl.InputStreamService;
import com.fileshare.communication.service.impl.OutputStreamService;
import com.fileshare.concurrency.Parallel;
import com.fileshare.file.DirectoryWatcher;
import com.fileshare.file.FileInfo;
import com.fileshare.file.Packet;
import com.fileshare.file.io.InputStream;
import com.fileshare.file.io.OutputStream;
import com.fileshare.network.Address;
import com.fileshare.network.BindingHandler;
import com.fileshare.network.Connection;
import com.fileshare.network.ConnectionPool;
import com.fileshare.time.Clock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public class PeerService {
    private final static Logger logger = LogManager.getLogger(PeerService.class.getName());

    public interface IPeer extends Remote {
        public java.io.OutputStream getOutputStream(File f)
                throws IOException, RemoteException;

        public java.io.InputStream getInputStream(File f)
                throws IOException, RemoteException;

        public void delete(File f)
                throws IOException, RemoteException;

        public void fusion(Address address)
                throws IOException, RemoteException;

        @Deprecated
        public void receive(Packet packet)
                throws RemoteException;
    }

    public static class Peer extends UnicastRemoteObject implements IPeer, Observer {
        Address address;
        BindingHandler bindingHandler;
        DirectoryWatcher directoryWatcher;
        Clock clock;
        volatile ConnectionPool connections;

        public Peer(String name) throws RemoteException, AlreadyBoundException {
            super();
            this.address = new Address(name);
            clock = new Clock(address.toString());
            connections = new ConnectionPool(clock, address);
            logger.info("New peer: " + name);
            this.bindingHandler = new BindingHandler(name, this);
            this.directoryWatcher = new DirectoryWatcher("./", 4, clock);
            this.directoryWatcher.addObserver(Peer.this);
            this.directoryWatcher.watchDir();
        }

        public void start() throws Exception {
            logger.info("Binding network interface");
            bindingHandler.bind();
            //new Thread(connections).start();
        }

        public void stop() throws Exception {
            logger.info("Unbinding network interface");
            bindingHandler.unbind();
        }

        @Override
        public java.io.OutputStream getOutputStream(File f) throws IOException, RemoteException {
            return new OutputStream(new OutputStreamService(new FileOutputStream(f)));
        }

        @Override
        public java.io.InputStream getInputStream(File f) throws IOException, RemoteException {
            return new InputStream(new InputStreamService(new FileInputStream(f)));
        }

        @Override
        public void delete(final File f) throws IOException, RemoteException {
            if (connections.size() > 0) {
                Parallel.For(connections.size(), connections, new Parallel.Operation<Connection>() {
                    @Override
                    public void perform(Connection connection) {
                        clock.incrementClock();
                        logger.info("Deleting file:\nName: " + f.getName()
                                + " From: " + connection.getAddress());

                        connection.delete(f);
                    }
                });
            }
        }

        @Override
        public void fusion(Address address) throws IOException, RemoteException {
            connections.add(new Connection(address));
            logger.info("New fusion:" + address.toString());
        }

        //TODO only for KMich ;>
        public void broadcast(final File file) throws IOException {
            if (connections.size() > 0) {
                for (Connection connection : connections) {
                    //Parallel.For(connections.size(), connections, new Parallel.Operation<Connection>() {
                    //@Override
                    //public void perform(Connection connection) {
                    clock.incrementClock();
                    logger.info("Uploading file:\nName: " + file.getName()
                            + "\nFrom: " + address.toString()
                            + " To: " + connection.getAddress());
                    long len = file.length();
                    long t = System.currentTimeMillis();
                    try {
                        connection.upload(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    t = (System.currentTimeMillis() - t) / 1000;

                    if (len <= 0)
                        len = 1;

                    logger.info("Upload: " + file.getName() + " witch " + (len / t / 1000000d) +
                            " MB/s");
                }
                //});
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            HashMap<String, FileInfo> paths = (HashMap<String, FileInfo>) arg;
            Set<String> fileList = paths.keySet();

            for (FileInfo fileInfo : paths.values()) {
                if (fileInfo.getFlag() == FileInfo.FLAG_DELETED) {
                    try {
                        delete(fileInfo.getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        broadcast(fileInfo.getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void receive(Packet packet) throws RemoteException {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(packet.getName());
                packet.writeTo(fileOutputStream);
                fileOutputStream.close();
                fileOutputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO only for KMich ;>
    public static void main(String[] args) throws Exception {
        String name;
        if (args.length > 0)
            name = args[0];
        else
            name = "peer";

        Peer peer = new Peer(name);
        peer.start();
        if (args.length > 1) {
            logger.info("Broadcast!");
            try {
                RandomAccessFile f = new RandomAccessFile("1MB", "rw");
                f.setLength(1024 * 1024);
                f.close();
                File testFile = new File("1MB");
                peer.broadcast(testFile);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
