package com.fileshare.file.io;

import java.io.File;
import java.rmi.RemoteException;

/**
 * @author Jan Paw
 *         Date: 6/30/13
 */
public interface FileSystem {
    public boolean delete(File f)
            throws SecurityException, RemoteException;

    public boolean upload(File f)
            throws NullPointerException, RemoteException;

    public boolean download(File f)
            throws NullPointerException, RemoteException;
}
