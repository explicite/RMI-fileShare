package com.fileshare.communication;

import com.fileshare.concurrency.Parallel;
import com.fileshare.file.DirectoryWatcher;
import com.fileshare.file.Packet;
import com.fileshare.file.io.InputStream;
import com.fileshare.file.io.OutputStream;
import com.fileshare.file.util.FileInfo;
import com.fileshare.network.Address;
import com.fileshare.network.BindingHandler;
import com.fileshare.network.Connection;
import com.fileshare.network.ConnectionPool;
import com.fileshare.time.Clock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.rmi.AlreadyBoundException;
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

    public static class LocalPeer extends UnicastRemoteObject implements Peer, Observer {
        Address address;
        BindingHandler bindingHandler;
        DirectoryWatcher directoryWatcher;
        Clock clock;
        volatile ConnectionPool connections;

        public LocalPeer(String name) throws RemoteException, AlreadyBoundException {
            super();
            this.address = new Address(name);
            clock = new Clock(address.toString());
            logger.info("New peer: " + name);
            this.bindingHandler = new BindingHandler(name, this);
            this.directoryWatcher = new DirectoryWatcher("./", 4, clock);
            this.directoryWatcher.addObserver(LocalPeer.this);
            this.directoryWatcher.watchDir();
        }

        public void start() throws Exception {
            logger.info("Binding network interface");
            bindingHandler.bind();
            connections = new ConnectionPool(clock, address);
            new Thread(connections).start();
        }

        public void stop() throws Exception {
            logger.info("Unbinding network interface");
            bindingHandler.unbind();
        }

        @Override
        public java.io.OutputStream getOutputStream(File f) throws IOException, RemoteException {
            return new OutputStream(new RemoteOutputStream(new FileOutputStream(f)));
        }

        @Override
        public java.io.InputStream getInputStream(File f) throws IOException, RemoteException {
            return new InputStream(new RemoteInputStream(new FileInputStream(f)));
        }

        @Override
        public void delete(final File f) throws IOException, RemoteException {
            if (connections.size() > 0) {
                Parallel.For(connections.size(), connections, new Parallel.Operation<Connection>() {
                    @Override
                    public void perform(Connection connection) {
                        if (connection.delete(f)) {
                            clock.incrementClock();
                            logger.info("Deleting file:\nName: " + f.getName()
                                    + " From: " + connection.getAddress());
                        } else
                            logger.error("ERROR: Deleting file:\nName: " + f.getName()
                                    + " From: " + connection.getAddress());
                    }
                });
            }
        }

        @Override
        public boolean fusion(Address address) throws IOException, RemoteException {
            if (connections.add(new Connection(address))) {
                logger.info("New fusion:" + address.toString());
                return true;
            } else {
                logger.error("ERROR: Fusion fail => " + address.toString());
                return false;
            }
        }

        //TODO only for KMich ;>
        public void broadcast(final File file) throws IOException {
            if (connections.size() > 0) {
                Parallel.For(connections.size(), connections, new Parallel.Operation<Connection>() {
                    @Override
                    public void perform(Connection connection) {
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

                        if (t <= 0)
                            t = 1;

                        logger.info("Upload: " + file.getName() + " witch " + (len / t / 1000000d) +
                                " MB/s");
                    }
                }
                );
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
            name = "localPeer";

        LocalPeer localPeer = new LocalPeer(name);
        localPeer.start();
        if (args.length > 1) {
            logger.info("Broadcast!");
            try {
                RandomAccessFile f = new RandomAccessFile("1MB", "rw");
                f.setLength(1024 * 1024);
                f.close();
                File testFile = new File("1MB");
                localPeer.broadcast(testFile);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
