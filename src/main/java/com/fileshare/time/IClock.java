package com.fileshare.time;

import java.util.Map;

/**
 * @author Mateusz Kwiecien
 *         Date: 06.06.13
 */
public interface IClock {
    public int getNodeId();
    public void setNodeId(int clientId);
    public void addNode(Integer id, Integer state);
    public Map<Integer, Integer> getVector();
    public void setVector(Map<Integer, Integer> vector);
    public int getSize();
    public void incrementClock();
    public boolean equivalent(Clock current);
    public boolean isGreater(Clock current);
    public boolean isGreaterOrEqual(Clock current);
    public boolean isLower(Clock current);
    public void consolidate(Clock current);
}
