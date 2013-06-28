package com.fileshare.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Provides method to generate dummy file for tests.
 *
 * @author Kamil Sikora
 *         Data: 13.06.2013
 */
public class DummyFile {

    /**
     * @param size size in bytes.
     */
    public static File generateFile(int size) {
        File file = new File("./test" + "//" + getFileName(size));

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.setLength(1024 * size);
            raf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private static String getFileName(int size) {
        String unit = "KB";

        if (size < 1024 * 1024)
            unit = "MB";
        if (size > 1024 * 1024)
            unit = "GB";

        String number = unit.equals("GB") ? String.valueOf(size / 1024 / 1024) : String.valueOf(size / 1024);

        return number + unit;
    }

}