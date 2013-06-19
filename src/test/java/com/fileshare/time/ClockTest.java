package com.fileshare.time;
import junit.framework.Assert;
import org.junit.Test;

//import static org.junit.Assert.assertTrue;

/**
 * @author Mateusz Kwiecien
 *         Date: 12.06.13
 */
public class ClockTest {

    @Test
    public void testCreate() throws Exception {
        Long Id1 = Long.valueOf(1);
        Clock clock1 = new Clock(Id1);
        Assert.assertNotNull(clock1);
    }

    @Test
    public void testEquivalent() throws Exception {
        Long Id1 = Long.valueOf(1);
        Long Id2 = Long.valueOf(2);
        Clock cl1 = new Clock(Id1);
        Clock cl2 = new Clock(Id2);
        cl1.addNode(Id1, 1);
        cl1.addNode(Id2, 2);//cl1 = {1,2}
        cl2.addNode(Id1, 2);
        cl2.addNode(Id2, 1);//cl2 = {2,1}
        Assert.assertTrue(cl1.equivalent(cl2));
    }

    @Test
    public void testIsGreater() throws Exception {

        Long Id1 = Long.valueOf(1);
        Long Id2 = Long.valueOf(2);
        Clock cl1 = new Clock(Id1);
        Clock cl2 = new Clock(Id2);
        cl1.addNode(Id1, 1);
        cl1.addNode(Id2, 1);
        cl2.addNode(Id1, 0);
        cl2.addNode(Id2, 0);
        cl1.incrementClock();
        Assert.assertTrue (cl1.isGreater(cl2));
    }

    @Test
    public void testIsGreaterOrEqual() throws Exception {
        Long Id1 = Long.valueOf(1);
        Long Id2 = Long.valueOf(2);
        Clock cl1 = new Clock(Id1);
        Clock cl2 = new Clock(Id2);
        cl1.addNode(Id1, 0);
        cl1.addNode(Id2, 1);
        cl2.addNode(Id1, 0);
        cl2.addNode(Id2, 1);
        cl1.incrementClock();
        Assert.assertTrue(cl1.isGreaterOrEqual(cl2));
    }

    @Test
    public void testIsLower()  {
        Long Id1 = Long.valueOf(1);
        Long Id2 = Long.valueOf(2);
        Clock cl1 = new Clock(Id1);
        Clock cl2 = new Clock(Id2);
        cl1.addNode(Id1, 0);
        cl1.addNode(Id2, 0);
        cl2.addNode(Id1, 2);
        cl2.addNode(Id2, 2);
        cl2.incrementClock();
        Assert.assertTrue (cl1.isLower(cl2));
    }

    @Test
    public void testEquals() throws Exception {
        Long Id1 = Long.valueOf(1);
        Long Id2 = Long.valueOf(2);
        Clock clock1 = new Clock(Id1);
        clock1.addNode(Id2,0);
        Clock clock2 = new Clock(Id2);
        clock2.addNode(Id1,0);
        Assert.assertTrue(clock1.equals(clock2));
    }

    @Test
    public void testNotEquals() throws Exception {
        Long Id1 = Long.valueOf(1);
        Long Id2 = Long.valueOf(2);
        Clock clock1 = new Clock(Id1);
        clock1.addNode(Id2,2);
        Clock clock2 = new Clock(Id2);
        clock2.addNode(Id1,1);
        Assert.assertTrue(!clock1.equals(clock2));
    }

    @Test
    public void testGetSize() throws Exception {
        Long Id1 = Long.valueOf(1);
        Long Id2 = Long.valueOf(2);
        Clock cl = new Clock(Id1);//create clock with id equals 1
        Assert.assertEquals(1, cl.getSize());
    }

    @Test
    public void testSetVector() throws Exception {
        Long Id1 = Long.valueOf(1);
        Long Id2 = Long.valueOf(2);
        Clock c1 = new Clock(Id1);
        Clock c2 = new Clock(Id2);
        c1.addNode(Id2, 0);
        c2.setVector(c1.getVector());

        Assert.assertEquals(c1.getVector(), c2.getVector());
    }

    @Test
    public void testSetId() throws Exception {
        Long oldId = Long.valueOf(1);
        Long newId = Long.valueOf(2);
        Clock cl = new Clock(oldId);
        cl.setNodeId(newId);
        Assert.assertEquals(newId, cl.getNodeId());
    }
}
