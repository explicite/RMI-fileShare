package com.fileshare.file;

import com.fileshare.time.Clock;
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
    Clock clock;

    @Before
    public void createDirectoryWatcher() {
        clock = new Clock("aa");
        directoryWatcher = new DirectoryWatcher("./test", 1, clock);

        eventObserver = new EventObserver();
        directoryWatcher.addObserver(eventObserver);

        new Thread(directoryWatcher).start();
    }

    @Test
    public void doesItEvenWorkTest(){
        File dummy = DummyFile.generateFile("./test", 5096);
        while(eventObserver.getCaughtEvent() == 0){}
        System.out.println(eventObserver.caughtEvent);
    }

    @Test
    public void observerTest() {
       /* testFile = DummyFile.generateFile("./test", 1024);

        while (eventObserver.getCaughtEvent() == 0) {
        }
        assertTrue(eventObserver.getCaughtEvent() == FileInfo.FLAG_CREATED);*/
    }

    @After
    public void clean() {
//        testFile.delete();
    }

    private class EventObserver implements Observer {
        private int caughtEvent;

        public int getCaughtEvent() {
            return caughtEvent;
        }

        @Override
        public void update(Observable o, Object arg) {
            ArrayList<FileInfo> changes = (ArrayList<FileInfo>) arg;
            FileInfo pathInfo = changes.get(0);
            caughtEvent = pathInfo.getFlag();
        }
    }
}
