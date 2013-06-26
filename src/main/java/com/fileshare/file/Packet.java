package com.fileshare.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

/**
 * @author Jan Paw
 *         Date: 6/26/13
 */
public class Packet implements Serializable {
    private String name;
    private byte[] data;

    public Packet(File file) {
        this.name = file.getName();
        try {
            data = new byte[(int) (file.length())];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(data);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void writeTo(java.io.OutputStream out) {
        try {
            out.write(data);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
