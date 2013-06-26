package com.fileshare.network;

import com.fileshare.communication.PeerService;
import com.fileshare.util.ClockObservable;
import com.fileshare.util.ClockObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Jan Paw
 *         Date: 6/26/13
 */
public class ConnectionPool extends LinkedList<Connection> implements Runnable, ClockObservable {
    private final static Logger logger = LogManager.getLogger(PeerService.class.getName());
    private static final long serialVersionUID = -6447733757552047292L;
    private ClockObserver clock;
    volatile boolean inAction = false;

    public ConnectionPool(ClockObserver clock, Address address) {
        super();
        this.clock = clock;
        inAction = true;
        for (Connection connection : Scanner.scan()) {
            try {
                connection.fusion(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.add(connection);
            clock.add(connection.getAddress());
        }
        inAction = false;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            for (Connection connection : this)
                if (!connection.isReachable(Connection.TIMEOUT * 10)) {
                    this.remove(connection);
                    clock.remove(connection.getAddress());
                    logger.info("Lost connection with: " + connection.getAddress());
                }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @Override
    public boolean add(Connection connection) {
        if (this.contains(connection))
            return false;

        clock.add(connection.getAddress());
        return super.add(connection);
    }

    @Override
    public boolean remove(Object o) {
        clock.remove(((Connection) o).getAddress());
        return super.remove(o);
    }

    @Override
    public void setClock(ClockObserver clockObserver) {
        this.clock = clockObserver;
    }
}
