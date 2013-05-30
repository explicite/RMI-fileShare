package com.fileshare.communication;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
public class Address {
    private final Integer nodeId;
    private final InetAddress inetAddress;

    //TODO dynamiczne wyszukiwanie node'ow
    @Deprecated
    public Address() throws UnknownHostException {
        nodeId = 0;
        inetAddress = InetAddress.getLocalHost();
    }

    public Address(Integer nodeId) throws UnknownHostException {
        inetAddress = InetAddress.getLocalHost();
        this.nodeId = nodeId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    @Override
    public String toString() {
        return Integer.toString(nodeId) + "://";
    }
}
