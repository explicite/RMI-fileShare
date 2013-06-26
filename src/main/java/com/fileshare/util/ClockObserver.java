package com.fileshare.util;

import com.fileshare.network.Address;

/**
 * @author Jan Paw
 *         Date: 6/26/13
 */
public interface ClockObserver {
    void add(Address address);

    void remove(Address address);
}
