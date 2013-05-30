package com.fileshare.node;

import com.fileshare.communication.Address;
import com.fileshare.communication.Connection;
import com.fileshare.file.RemoteFile;
import com.fileshare.time.Clock;

import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
public abstract class Node {
    private final Address address;
    private Clock clock;
    private LinkedList<Connection> connections;
    private LinkedList<RemoteFile> files;

    protected Node() throws UnknownHostException {
        this.address = new Address();
    }
}
