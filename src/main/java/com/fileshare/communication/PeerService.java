package com.fileshare.communication;

import com.fileshare.communication.service.impl.InputStreamService;
import com.fileshare.communication.service.impl.OutputStreamService;
import com.fileshare.file.DirectoryWatcher;
import com.fileshare.file.FileInfo;
import com.fileshare.file.Packet;
import com.fileshare.file.io.InputStream;
import com.fileshare.file.io.OutputStream;
import com.fileshare.network.Address;
import com.fileshare.network.BindingHandler;
import com.fileshare.network.Connection;
import com.fileshare.network.Scanner;
import com.fileshare.time.Clock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
public class PeerService {
    private final static Logger logger = LogManager.getLogger(PeerService.class.getName());
    private volatile static LinkedList<Connection> connections = new LinkedList<>();

    public interface IPeer extends Remote {
        public java.io.OutputStream getOutputStream(File f)
                throws IOException, RemoteException;

        public java.io.InputStream getInputStream(File f)
                throws IOException, RemoteException;

        public void receive(Packet packet)
                throws RemoteException;
    }

    public static class Peer extends UnicastRemoteObject implements IPeer, Observer {
        Address address;
        BindingHandler bindingHandler;
        DirectoryWatcher directoryWatcher;
        Clock clock;

        public Peer(String name) throws RemoteException, AlreadyBoundException {
            super();
            this.address = new Address(name);
            clock = new Clock(address.toString());
            logger.info("New peer: " + name);
            this.bindingHandler = new BindingHandler(name, this);
            /*this.directoryWatcher = new DirectoryWatcher("./", 4, clock);
            this.directoryWatcher.addObserver(Peer.this);
            new Thread(directoryWatcher).start();*/
        }

        public void start() throws Exception {
            logger.info("Binding network interface");
            bindingHandler.bind();
            connections = Scanner.scan(); //TODO periodic inv
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

        //TODO only for KMich ;>
        public void broadcast(final File file) throws IOException {
            for (Connection connection : connections) {
                clock.incrementClock();
                logger.info("Uploading file:\nName: " + file.getName()
                        + "\nFrom: " + address.toString()
                        + " To: " + connection.getAddress());
                long len = file.length();
                long t = System.currentTimeMillis();
                connection.send(file);
                t = (System.currentTimeMillis() - t) / 1000;

                if (t <= 0)
                    t = file.length();

                logger.info("Upload: " + file.getName() + " witch " + (len / t / 1000000d) +
                        " MB/s");
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            ArrayList<FileInfo> paths = (ArrayList<FileInfo>) arg;
            clock.incrementClock();
            for (FileInfo path : paths) {
                try {
                    broadcast(path.getFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void receive(Packet packet) throws RemoteException {
            try {
                packet.writeTo(new FileOutputStream(packet.getName()));
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
                File testFile = new File("1MB");
                peer.broadcast(testFile);
                peer.broadcast(testFile);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        /*Thread.sleep(5 * 60 * 1000);
        peer.stop();*/
    }
}
