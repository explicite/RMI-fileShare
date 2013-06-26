package com.fileshare.network;

import com.fileshare.communication.PeerService;
import com.fileshare.file.Packet;
import com.fileshare.file.io.InputStream;
import com.fileshare.file.io.OutputStream;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Jan Paw
 *         Date: 6/12/13
 */
public class Connection implements Serializable {
    public final static int BUF_SIZE = 1024 * 64;
    public final static int TIMEOUT = 2000;
    private Address address;
    private PeerService.IPeer peer = null;
    private Registry registry;

    public synchronized boolean isReachable(int timeout) {
        return address.isReachable(timeout);
    }

    public Connection(Address address) {
        this.address = address;
        if (isReachable(TIMEOUT)) {
            try {
                registry = LocateRegistry.getRegistry(address.getInetAddress().getHostAddress());
                peer = (PeerService.IPeer) registry.lookup(address.toString());
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void copy(java.io.InputStream in, java.io.OutputStream out)
            throws IOException {

        if (in instanceof InputStream) {
            ((InputStream) in).transfer(out);
        }

        if (out instanceof OutputStream) {
            ((OutputStream) out).transfer(in);
        }

        byte[] b = new byte[BUF_SIZE];
        int len;
        while ((len = in.read(b)) >= 0) {
            out.write(b, 0, len);
        }

        out.close();
        in.close();
    }

    public synchronized void upload(File src) throws IOException {
        copy(new FileInputStream(src),
                peer.getOutputStream(new File(src.getName() + System.currentTimeMillis())));
    }

    @Deprecated
    public synchronized void send(File src) throws IOException {
        peer.receive(new Packet(src));
    }

    public synchronized void download(File destination) throws IOException {
        copy(peer.getInputStream(destination),
                new FileOutputStream(destination));
    }

    public Address getAddress() {
        return address;
    }

    public synchronized boolean delete(File f) {
        File local = new File(f.getName()); // czy to zadzia?a?
        return local.delete();
    }
}
