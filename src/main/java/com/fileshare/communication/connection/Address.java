package com.fileshare.communication.connection;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
@Deprecated //TODO global address for naming friendly - toString and attr
public class Address {
    private final Integer nodeId;
    private final InetAddress inetAddress;

    public Address(Integer nodeId) throws UnknownHostException {
        inetAddress = InetAddress.getLocalHost();
        this.nodeId = nodeId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    @Override
    public String toString() {
        return "//" + inetAddress.toString() + nodeId;
    }
}
