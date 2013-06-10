package com.fileshare.file;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @author Jan Paw
 *         Date: 6/10/13
 */
class MD5 {
    private static byte[] createChecksum(String fileName) throws Exception {
        InputStream inputStream = new FileInputStream(fileName);

        byte[] buffer = new byte[1024];
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = inputStream.read(buffer);
            if (numRead > 0) {
                messageDigest.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        inputStream.close();
        return messageDigest.digest();
    }

    public static String getChecksum(String filename) throws Exception {
        byte[] bytes = createChecksum(filename);
        String result = "";

        for (byte aByte : bytes) {
            result += Integer.toString((aByte & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}