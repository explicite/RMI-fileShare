package com.fileshare.file;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;

import static org.junit.Assert.assertTrue;


/**
 * @author Kamil Sikora
 *         Data: 13.06.13
 */
public class PathInfoTest {
    Path p;
    PathInfo pi;

    @Before
    public void preparePath() {
        p = new File("./").toPath();
    }

    @Test
    public void createEventKindTest() {
        pi = new PathInfo(p, StandardWatchEventKinds.ENTRY_CREATE);
        assertTrue(pi.getFlag() == PathInfo.FLAG_CREATED);
    }

    @Test
    public void modifyEventKindTest() {
        pi = new PathInfo(p, StandardWatchEventKinds.ENTRY_MODIFY);
        assertTrue(pi.getFlag() == PathInfo.FLAG_MODIFIED);
    }

    @Test
    public void deleteEventKindTest() {
        pi = new PathInfo(p, StandardWatchEventKinds.ENTRY_DELETE);
        assertTrue(pi.getFlag() == PathInfo.FLAG_DELETED);
    }
}
