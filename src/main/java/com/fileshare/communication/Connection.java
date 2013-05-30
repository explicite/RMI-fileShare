package com.fileshare.communication;

import java.util.Queue;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
public class Connection implements IConnection, Runnable {
    private Queue<Message> buffer;

    @Override
    public Message receive() {
        return null;  //TODO
    }

    @Override
    public void send(Message message) {
        //TODO
    }

    @Override
    public void run() {
        //TODO petla do sprawdzania buffer
    }
}
