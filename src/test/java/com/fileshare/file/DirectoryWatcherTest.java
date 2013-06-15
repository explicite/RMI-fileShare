package com.fileshare.file;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertTrue;

/**
 * @author Kamil Sikora
 *         Data: 13.06.13
 */
public class DirectoryWatcherTest {
    DirectoryWatcher directoryWatcher;
    EventObserver eventObserver;
    File testFile;

    @Before
    public void createDirectoryWatcher() {
        directoryWatcher = new DirectoryWatcher("./test", 1);

        eventObserver = new EventObserver();
        directoryWatcher.addObserver(eventObserver);

        new Thread(directoryWatcher).start();
    }

    @Test
    public void observerTest() {
        testFile = DummyFile.generateFile("./test", 1024);

        while (eventObserver.getCaughtEvent() == 0) {
        }
        assertTrue(eventObserver.getCaughtEvent() == PathInfo.FLAG_CREATED);
    }

    @After
    public void clean(){
        testFile.delete();
    }

    @Test
    public void fileCreatedTest() throws InterruptedException {
        testFile = DummyFile.generateFile("./test", 1024);

        int event = eventObserver.getCaughtEvent();
        assertTrue(event == PathInfo.FLAG_CREATED);
    }

    @Test
    public void fileModifiedTest() {

    }

    @Test
    public void fileDeletedTest() {

    }



    private class EventObserver implements Observer {
        private int caughtEvent;

        public int getCaughtEvent() {
            return caughtEvent;
        }

        @Override
        public void update(Observable o, Object arg) {
            ArrayList<PathInfo> changes = (ArrayList<PathInfo>) arg;
            PathInfo pathInfo = changes.get(0);
            caughtEvent = pathInfo.getFlag();
        }
    }
}
