package com.fileshare.node;

import java.net.InetAddress;
import java.rmi.Remote;

/**
 * @author Jan Paw
 *         Date: 5/28/13
 */
public interface INode extends Remote {
    public void connectToServer(InetAddress inetAddress);
}
