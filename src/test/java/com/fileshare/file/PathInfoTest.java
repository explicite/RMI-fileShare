package com.fileshare.file;

import com.fileshare.file.util.FileInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.StandardWatchEventKinds;

import static org.junit.Assert.assertTrue;


/**
 * @author Kamil Sikora
 *         Data: 13.06.13
 */
public class PathInfoTest {
    File p;
    FileInfo pi;

    @Before
    public void preparePath() {
        p = new File("./");
    }

    @Test
    public void createEventKindTest() {
        pi = new FileInfo(p, StandardWatchEventKinds.ENTRY_CREATE);
        assertTrue(pi.getFlag() == FileInfo.FLAG_CREATED);
    }

    @Test
    public void modifyEventKindTest() {
        pi = new FileInfo(p, StandardWatchEventKinds.ENTRY_MODIFY);
        assertTrue(pi.getFlag() == FileInfo.FLAG_MODIFIED);
    }

    @Test
    public void deleteEventKindTest() {
        pi = new FileInfo(p, StandardWatchEventKinds.ENTRY_DELETE);
        assertTrue(pi.getFlag() == FileInfo.FLAG_DELETED);
    }
}
