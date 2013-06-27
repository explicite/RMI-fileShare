package com.fileshare.file;

import com.fileshare.communication.PeerService;
import com.fileshare.time.Clock;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * @author Kamil Sikora
 *         Data: 10.06.13
 */
public class DirectoryWatcher extends IDirectoryWatch {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PeerService.class.getName());
    private final Map<WatchKey, Path> keys;
    private final HashMap<String, FileInfo> newChangesBuffer;
    private Path path;
    private final Clock clock;
    private ArrayList<FileInfo> changesBuffer;
    private long lastUpdateTime;
    private volatile  Set<String> filesToIgnore;

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
        this.changesBuffer = new ArrayList<>();
        this.newChangesBuffer = new HashMap<String, FileInfo>();
        this.filesToIgnore = new HashSet<>();
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
            try {
                Thread.sleep(1000 * interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
                changedItems.add(new FileInfo(name, ev.kind()));
            }

            addToBuffer(changedItems);

            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                logger.info("Key removed" + key);
            }


        }
    }

    public void watchDir() {
        try {
            final WatchKey bDirWatchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
            new Thread(new Runnable() {
                public void run() {

                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        HashMap<String, FileInfo> changedItems = new HashMap<>();
                        List<WatchEvent<?>> events = bDirWatchKey.pollEvents();
                        for (WatchEvent<?> event : events) {
                            WatchEvent<Path> eventItem = cast(event);
                            logger.info(path.getFileName() + " event: #" + event.count() + "," + event.kind() + " File=" + event.context());
                            String fileName = eventItem.context().toFile().getName();
                            if (filesToIgnore.contains(fileName)) {
                                filesToIgnore.remove(fileName);
                                continue;
                            } else
                                changedItems.put(fileName, new FileInfo(eventItem.context().toFile(), event.kind()));
                        }
                        addToBuffer(changedItems);
                    }
                }
            }).start();
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    private boolean addToBuffer(HashMap<String, FileInfo> changedItems) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = System.currentTimeMillis();
            changesBuffer.clear();
        } else if (System.currentTimeMillis() - lastUpdateTime > 5000) {
            newChangesBuffer.putAll(changedItems);
            sendChanges();
            lastUpdateTime = 0;

            return true;
        }
        newChangesBuffer.putAll(changedItems);

        return false;
    }

    private boolean addToBuffer(ArrayList<FileInfo> changedItems) {
        if (lastUpdateTime == 0) {
            lastUpdateTime = System.currentTimeMillis();
            changesBuffer.clear();
        } else if (System.currentTimeMillis() - lastUpdateTime > 5000) {
            changesBuffer.addAll(changedItems);
            sendChanges();
            lastUpdateTime = 0;

            return true;
        }
        changesBuffer.addAll(changedItems);

        return false;
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
    public void sendChanges() {
        setChanged();
        notifyObservers(newChangesBuffer.clone());
        newChangesBuffer.clear();
        clearChanged();
    }

    @Override
    public void addFilesToIgnore(Set<String> fileList) {
        filesToIgnore.addAll(fileList);
    }

    @Override
    public void addFileToIgnore(String fileName) {
        filesToIgnore.add(fileName);
    }

    @Override
    public void run() {
//        this.watchChanges();
        watchDir();
    }

    public Set<String> getFilesToIgnore() {
        return filesToIgnore;
    }
}
