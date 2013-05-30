package com.fileshare.communication;

import java.rmi.Remote;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
public interface IConnection extends Remote {
    public Message receive();

    public void send(Message message);
}
