package com.fileshare.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Jan Paw
 *         Date: 6/12/13
 */
public class Address {
    public final static int PORT = 1099;
    private InetAddress inetAddress;
    private String name;

    public Address(InetAddress inetAddress, String name) {
        this.inetAddress = inetAddress;
        this.name = name;
    }

    public Address(String name) {
        this.inetAddress = getHostAddress();
        this.name = name;
    }

    public Address(String host, String name) {
        try {
            this.inetAddress = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.name = name;
    }

    private static InetAddress getHostAddress() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return "rmi://" + inetAddress.getHostAddress() + "/" + name;
    }

    public boolean isReachable(int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(inetAddress, Address.PORT), timeout);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return getAddress();
    }
}
