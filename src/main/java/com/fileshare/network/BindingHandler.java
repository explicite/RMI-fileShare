package com.fileshare.network;

import com.fileshare.communication.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

import static java.rmi.server.UnicastRemoteObject.unexportObject;

/**
 * @author Jan Paw
 *         Date: 6/12/13
 */
public class BindingHandler {
    private final static Logger logger = LogManager.getLogger(PeerService.class.getName());
    private LinkedList<Address> addressesToBind = new LinkedList<>();
    private Registry registry;
    private PeerService.IPeer peer;

    public BindingHandler(String name, PeerService.IPeer peer) {
        this.peer = peer;

        for (InetAddress inetAddress : NetworkInterfaces.networkInterfaces)
            addressesToBind.add(new Address(inetAddress, name));

        try {
            registry = LocateRegistry.createRegistry(1099);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bind() {
        if (addressesToBind.size() > 0) {
            for (Address address : addressesToBind) {
                if (address.isReachable(Connection.TIMEOUT)) {
                    try {
                        registry.bind(address.toString(), peer);
                        logger.info(address);
                    } catch (RemoteException | AlreadyBoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void unbind() {
        if (addressesToBind.size() > 0) {
            for (Address address : addressesToBind) {
                try {
                    registry.unbind(address.toString());
                } catch (RemoteException | NotBoundException e) {
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
