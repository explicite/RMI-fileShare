package com.fileshare.time;

import java.util.Map;

/**
 * @author Mateusz Kwiecien
 *         Date: 06.06.13
 */
public interface IClock {
    public String getNodeId();
    public void setNodeId(String clientId);
    public void addNode(String id, Integer state);
    public Map<String, Integer> getVector();
    public void setVector(Map<String, Integer> vector);

    /**
     *
     * @return size of clock (number of peers in network)
     */
    public int getSize();
    public void incrementClock();
    public boolean equivalent(Clock current);
    public boolean isGreater(Clock current);
    public boolean isGreaterOrEqual(Clock current);
    public boolean isLower(Clock current);
    public void consolidate(Clock current);
}
