package com.fileshare.file;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author Kamil Sikora
 *         Data: 13.06.13
 */
public class DirectoryWatcherTest {
    File testFile;

    @Before
    public void createTestFile(){
        testFile = DummyFile.generateSize(1024);
    }

    @Test
    public void fileCreated(){

    }

    @Test
    public void fileModified(){

    }

    @Test
    public void fileDeleted(){

    }
}
