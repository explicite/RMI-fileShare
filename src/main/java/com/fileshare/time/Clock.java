package com.fileshare.time;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Mateusz Kwiecien
 *         Date: 5/30/13
 */

public class Clock implements IClock {
    private Map<Integer, Integer> vector = new HashMap<>();
    private int nodeId;

    public Clock(Integer id) {
        this.nodeId = id;
        this.vector.put(id, 0);
    }

    @Override
    public int getNodeId() {
        return nodeId;
    }

    @Override
    public void setNodeId(int clientId) {
        this.nodeId = clientId;
    }

    @Override
    public void addNode(Integer id, Integer state) {
        if (!vector.containsKey(id)) {
            vector.put(id, state);
        }
    }

    @Override
    public Map<Integer, Integer> getVector() {
        return vector;
    }

    @Override
    public void setVector(Map<Integer, Integer> vector) {
        this.vector = vector;
    }

    @Override
    public int getSize() {
        return vector.size();
    }

    @Override
    public void incrementClock() {
        if (vector.containsKey(this.nodeId)) {
            vector.put(this.nodeId, (vector.get(this.nodeId) + 1));
        }
    }

    @Override
    public boolean equivalent(Clock current) {
        return (!this.isGreater(current) && !this.isLower(current));
    }

    @Override
    public boolean isGreater(Clock current) {
        return (!this.equals(current) && this.isGreaterOrEqual(current));
    }

    @Override
    public boolean isGreaterOrEqual(Clock current) {
        if (current.equals(this)) {
            return false;
        } else {
            Iterator it = current.vector.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                    if (!((Integer) pairs.getValue() <= this.vector.get(pairs.getKey()))) {
                        return false;
                    }
                it.remove();
            }
            return true;
        }
    }

    @Override
    public boolean isLower(Clock current) {
        return (!this.equals(current) && current.isGreaterOrEqual(this));
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        Clock current = (Clock) obj;

        if (current.vector.size() == this.vector.size()) {
            Iterator it = current.vector.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                if (pairs.getValue() != this.vector.get(pairs.getKey())) {
                    return false;
                }
                it.remove();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void consolidate(Clock current) {
        // TODO : compare and update vector clock
    }

}

