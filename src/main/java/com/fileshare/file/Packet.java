package com.fileshare.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

/**
 * @author Jan Paw
 *         Date: 6/26/13
 */
@Deprecated
public class Packet implements Serializable {
    private static final long serialVersionUID = 7946700308200203095L;
    private final String name;
    private byte[] data;

    public Packet(File file) {
        this.name = file.getName();
    }

    public String getName() {
        return name;
    }

    public void readIn() {
        try {
            File file = new File(name);
            data = new byte[(int) (file.length())];
            (new FileInputStream(file)).read(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeTo(java.io.OutputStream out) {
        try {
            out.write(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
