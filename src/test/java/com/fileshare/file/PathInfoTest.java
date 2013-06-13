package com.fileshare.file;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;


/**
 * @author Kamil Sikora
 *         Data: 13.06.13
 */
public class PathInfoTest{

    @Test
    public void kindTest(){
        Path p = new File(".").toPath();

        PathInfo pi = new PathInfo(p, StandardWatchEventKinds.ENTRY_CREATE);
        assertTrue(pi.getFlag() == PathInfo.FLAG_CREATED);

        pi = new PathInfo(p, StandardWatchEventKinds.ENTRY_DELETE);
        assertTrue(pi.getFlag() == PathInfo.FLAG_DELETED);

        pi = new PathInfo(p, StandardWatchEventKinds.ENTRY_MODIFY);
        assertTrue(pi.getFlag() == PathInfo.FLAG_MODIFIED);
    }

}
