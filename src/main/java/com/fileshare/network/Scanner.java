package com.fileshare.network;

import com.fileshare.concurrency.Parallel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * @author Jan Paw
 *         Date: 6/8/13
 */
public enum Scanner {
    INSTANCE;

    public static void scan() {
        LinkedList<String> addresses = new LinkedList<>();
        String localPrefix = "0.0.0.";
        try {
            InetAddress address = InetAddress.getLocalHost();
            String hostAddress = address.getHostAddress();
            localPrefix = hostAddress.substring(0, hostAddress.lastIndexOf(".") + 1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        for (int host = 0; host < 256; host++) {
            addresses.add(localPrefix + host);
        }
        Parallel.For(256, addresses, new Parallel.Operation<String>() {
            @Override
            public void perform(String address) {
                connect(address);
            }
        });
    }

    private static void connect(String address) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address, 1099), 2000);
            System.out.println("Address: " + address); //TODO get info for network arch
        } catch (IOException e) {
        }
    }
}
