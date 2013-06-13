package com.fileshare.network;

import com.fileshare.communication.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;
import java.util.LinkedList;

import static java.rmi.server.UnicastRemoteObject.unexportObject;

/**
 * @author Jan Paw
 *         Date: 6/12/13
 */
public class BindingInterface {
    private static final Logger logger = LogManager.getLogger(PeerService.class.getName());
    private LinkedList<Address> addresses = new LinkedList<>();
    private Registry registry;
    private PeerService.IPeer peer;

    public BindingInterface(String name, PeerService.IPeer peer) {
        this.peer = peer;
        Enumeration<NetworkInterface> n = null;
        try {
            n = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (n.hasMoreElements()) {
            NetworkInterface e = n.nextElement();
            Enumeration<InetAddress> a = e.getInetAddresses();
            while (a.hasMoreElements()) {
                addresses.add(new Address(a.nextElement(), name));
            }
        }
        try {
            registry = LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bind() {
        if (addresses.size() > 0) {
            for (Address address : addresses) {
                if (address.isReachable(Connection.TIMEOUT)) {
                    try {
                        registry.bind(address.toString(), peer);
                        logger.info(address);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (AlreadyBoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void unbind() {
        if (addresses.size() > 0) {
            for (Address address : addresses) {
                try {
                    registry.unbind(address.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            unexportObject(peer, true);
            unexportObject(registry, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }
}
