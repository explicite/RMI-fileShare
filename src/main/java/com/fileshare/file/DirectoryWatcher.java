package com.fileshare.file;

import com.fileshare.communication.PeerService;
import com.fileshare.time.Clock;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kamil Sikora
 *         Data: 10.06.13
 */
public class DirectoryWatcher extends IDirectoryWatch {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PeerService.class.getName());
    private final Map<WatchKey, Path> keys;
    private Path path;
    private final Clock clock;

    /**
     * @param watchedDir ścieżka do obserwowania.
     * @param interval   co ile sekund sprawdzany będzie system plików
     * @param clock
     */
    public DirectoryWatcher(String watchedDir, int interval, final Clock clock) {
        super();
        this.interval = interval;
        this.keys = new HashMap<>();
        this.path = new File(watchedDir).toPath();
        this.clock = clock;
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            WatchKey key = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            keys.put(key, path);
        } catch (IOException e) {
            logger.error("Error while registering the directory.");
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    @Override
    protected void watchChanges() {
        ArrayList<FileInfo> changedItems;
        // You spin me round, round baby... infinite loop
        while (true) {
            changedItems = new ArrayList<>();
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null)
                continue;

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent<Path> ev = cast(event);
                File name = ev.context().toFile();
                while (!checkIsFileUsed(name))
                    changedItems.add(new FileInfo(name, ev.kind()));
            }

            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                logger.info("Key removed" + key);
                if (keys.isEmpty()) {
                    break;
                }
            }

            if (changedItems.size() > 0)
                sendChanges(changedItems);

           /* try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    public boolean checkIsFileUsed(File file) {
        boolean isFileUnlocked = false;
        try {
            org.apache.commons.io.FileUtils.touch(file);
            isFileUnlocked = true;
        } catch (IOException e) {
            isFileUnlocked = false;
        }

        return isFileUnlocked;
    }

    @Override
    public void sendChanges(ArrayList<FileInfo> files) {
        setChanged();
        notifyObservers(files);
        clearChanged();
    }

    @Override
    public void run() {
        watchChanges();
    }
}
