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
    protected WatchService watchService;
    protected int interval;

    protected IDirectoryWatch() {
    }

    /**
     * <code>watchChanges()</code> jest właściwą metodą monitorującą dany katalog. Aby uniknąć zeżarcia zasobów, sprawdzenie następuje co pewien, zdefiniowany wcześniej, czas.
     */
    protected abstract void watchChanges();

    /**
     * <p>Metoda wysyła do obiektu obserwującego zmiany, wykryte przez monitora plików.</p>
     * <p>TODO do dyskusji z osobą zajmującą się przesłaniem plików, jak przesyłać zmiany. Popchnięcie listy zmienionych plików, z użyciem wzorca Obserwator wydaje mi się najrozsądniejszym rozwiązaniem.</p>
     * @param files
     */
    public abstract void sendChanges(ArrayList<PathInfo> files);

}
