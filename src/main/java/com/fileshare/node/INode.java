package com.fileshare.node;

import java.net.InetAddress;
import java.rmi.Remote;

/**
 * @author Jan Paw
 *         Date: 5/28/13
 * @deprecated
 */
public interface INode extends Remote {
    public void connect(InetAddress inetAddress);
}
