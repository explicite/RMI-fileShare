package com.fileshare.file;

import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 * Class provides information about event kind encountered on file.
 *
 * @author Kamil Sikora
 *         Data: 12.06.13
 */
public class PathInfo {
    public static final int FLAG_MODIFIED = 1;
    public static final int FLAG_DELETED = 2;
    public static final int FLAG_CREATED = 4;

    private Path path;
    private int flag;

    public PathInfo(Path path, WatchEvent.Kind flag) {
        this.path = path;
        if (flag == StandardWatchEventKinds.ENTRY_CREATE)
            this.flag = FLAG_CREATED;
        if (flag == StandardWatchEventKinds.ENTRY_DELETE)
            this.flag = FLAG_DELETED;
        if (flag == StandardWatchEventKinds.ENTRY_MODIFY)
            this.flag = FLAG_MODIFIED;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
