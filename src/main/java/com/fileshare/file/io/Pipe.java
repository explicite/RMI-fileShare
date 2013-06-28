package com.fileshare.file.io;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * @author Jan Paw
 *         Date: 6/10/13
 */
public class Pipe implements Serializable {
    public final static int BUF_SIZE = 1024 * 64;
    private static int keySeed = 0;
    private static final Hashtable<Integer, java.io.OutputStream>
            registry = new Hashtable<>();
    private final transient int key;
    private transient java.io.InputStream in;
    private final transient boolean isOutputRegistration;

    public Pipe(int key, java.io.InputStream in) {
        this.key = key;
        this.in = in;
        isOutputRegistration = false;
    }

    public Pipe(java.io.OutputStream out) {
        isOutputRegistration = true;
        synchronized (registry) {
            key = keySeed++;
            registry.put(key, out);
        }
    }

    public int getKey() {
        if (!isOutputRegistration)
            throw new IllegalArgumentException(
                    "Not an OutputStream registration");
        return key;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (isOutputRegistration)
            registry.remove(key);
    }

    private void writeObject(ObjectOutputStream out) throws
            IOException {
        out.writeInt(key);
        byte[] b = new byte[BUF_SIZE];
        int len;
        do {
            len = in.read(b);
            out.writeInt(len);
            if (len >= 0)
                out.write(b, 0, len);
        } while (len >= 0);
    }

    private void readObject(ObjectInputStream in) throws
            IOException {
        int key = in.readInt();
        java.io.OutputStream out = registry.remove(key);
        byte[] b = new byte[BUF_SIZE];
        int len;
        do {
            len = in.readInt();
            if (len >= 0) {
                in.readFully(b, 0, len);
                out.write(b, 0, len);
            }
        } while (len >= 0);
    }
}
