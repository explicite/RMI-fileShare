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
        Clock clock1 = new Clock(1);
        Assert.assertNotNull(clock1);
    }

    @Test
    public void testEquivalent() throws Exception {
        Clock cl1 = new Clock(1);
        Clock cl2 = new Clock(2);
        cl1.addNode(1, 1);
        cl1.addNode(2, 2);//cl1 = {1,2}
        cl2.addNode(1, 2);
        cl2.addNode(2, 1);//cl2 = {2,1}
        Assert.assertTrue(cl1.equivalent(cl2));
    }

    @Test
    public void testIsGreater() throws Exception {
        int Id1 = 1;
        int Id2 = 2;
        Clock cl1 = new Clock(Id1);
        Clock cl2 = new Clock(Id2);
        cl1.addNode(1, 1);
        cl1.addNode(2, 1);
        cl2.addNode(1, 0);
        cl2.addNode(2, 0);
        cl1.incrementClock();
        Assert.assertTrue (cl1.isGreater(cl2));
    }

    @Test
    public void testIsGreaterOrEqual() throws Exception {
        int Id1 = 1;
        int Id2 = 2;
        Clock cl1 = new Clock(Id1);
        Clock cl2 = new Clock(Id2);
        cl1.addNode(1, 0);
        cl1.addNode(2, 1);
        cl2.addNode(1, 0);
        cl2.addNode(2, 1);
        cl1.incrementClock();
        Assert.assertTrue(cl1.isGreaterOrEqual(cl2));
    }

    @Test
    public void testIsLower()  {
        int Id1 = 1;
        int Id2 = 2;
        Clock cl1 = new Clock(Id1);
        Clock cl2 = new Clock(Id2);
        cl1.addNode(1, 0);
        cl1.addNode(2, 0);
        cl2.addNode(1, 2);
        cl2.addNode(2, 2);
        cl2.incrementClock();
        Assert.assertTrue (cl1.isLower(cl2));
    }

    @Test
    public void testEquals() throws Exception {

        Clock clock1 = new Clock(1);
        clock1.addNode(2,0);
        Clock clock2 = new Clock(2);
        clock2.addNode(1,0);
        Assert.assertTrue(clock1.equals(clock2));
    }

    @Test
    public void testNotEquals() throws Exception {
        Clock clock1 = new Clock(1);
        clock1.addNode(2,2);
        Clock clock2 = new Clock(2);
        clock2.addNode(1,1);
        Assert.assertTrue(!clock1.equals(clock2));
    }

    @Test
    public void testGetSize() throws Exception {
        Clock cl = new Clock(1);//create clock with id equals 1
        Assert.assertEquals(1, cl.getSize());
    }

    @Test
    public void testSetVector() throws Exception {
        Clock c1 = new Clock(1);
        Clock c2 = new Clock(2);
        c1.addNode(2, 0);
        c2.setVector(c1.getVector());

        Assert.assertEquals(c1.getVector(), c2.getVector());
    }

    @Test
    public void testSetId() throws Exception {
        int oldId = 1;
        Clock cl = new Clock(oldId);
        int newId = 2;
        cl.setNodeId(newId);
        Assert.assertEquals(newId, cl.getNodeId());
    }
}
