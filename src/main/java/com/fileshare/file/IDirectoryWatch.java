package com.fileshare.file;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Observable;

/**
 * <p>Abstrakcyjna klasa monitorująca dany katalog w poszukiwaniu zmian.</p>
 * TODO rekursywne monitorowanie katalogu (???)
 *
 * @author Kamil Sikora
 *         Data: 26.05.13
 */
public abstract class IDirectoryWatch extends Observable implements Runnable {
    private WatchService watchService;
    private int interval;

    protected IDirectoryWatch(Path watchDir, int interval) {
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.interval = interval;
    }

    /**
     * Metoda rejestruje dany katalog do monitorowania.
     */
    protected abstract void registerDir(Path dir);

    /**
     * Metoda rejestruje katalog i jego podkatalogi do monitorowania.
     * TODO czy robić?
     */
    protected abstract void registerDirRecursively(Path dir);

    /**
     * Metoda wymusza sprawdzenie systemu plików pod kątem zmian.
     */
    public abstract void scanForChanges();

    /**
     * <code>watchChanges()</code> jest właściwą metodą monitorującą dany katalog. Aby uniknąć zeżarcia zasobów, sprawdzenie następuje co pewien, zdefiniowany wcześniej, czas.
     */
    protected abstract void watchChanges();

    /**
     * <p>Metoda wysyła do obiektu obserwującego zmiany, wykryte przez monitora plików.</p>
     * <p>TODO do dyskusji z osobą zajmującą się przesłaniem plików, jak przesyłać zmiany. Popchnięcie listy zmienionych plików, z użyciem wzorca Obserwator wydaje mi się najrozsądniejszym rozwiązaniem.</p>
     */
    public void sendChanges(ArrayList<Path> files) {
        hasChanged();
        notifyObservers(files);
        clearChanged();
    }


}
