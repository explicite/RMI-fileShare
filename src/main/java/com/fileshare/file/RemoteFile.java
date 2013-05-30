package com.fileshare.file;

import java.io.File;
import java.net.URI;

/**
 * @author Jan Paw
 *         Date: 5/30/13
 */
public class RemoteFile {
    File file;

    public RemoteFile(URI uri) {
        //TODO
        //Nazwa pliku to rf://id_node/zwykle_uri
        file = new File(uri);
    }

    /**
     * TODO mechanizm synchronizacji
     */
    public void synchronize(RemoteFile remoteFile) {
        //if remoteFile != this
        // then if remoteFile > this
        this.file = remoteFile.getFile();
        // else powiadom ze mam wersje do dupy?
    }

    public File getFile() {
        return file;
    }
}
