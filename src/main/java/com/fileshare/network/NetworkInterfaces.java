package com.fileshare.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;

/**
 * @author Jan Paw
 *         Date: 6/13/13
 */
public enum NetworkInterfaces {
    INSTANCE;
    public final static LinkedList<InetAddress> networkInterfaces;

    static {
        networkInterfaces = new LinkedList<>();
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
                InetAddress inetAddress = a.nextElement();
                if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {  //Local network interfaces only
                    networkInterfaces.add(inetAddress);
                }
            }
        }
    }
}
