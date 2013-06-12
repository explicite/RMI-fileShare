import com.fileshare.communication.PeerService;
import com.fileshare.file.io.InputStream;
import com.fileshare.file.io.OutputStream;
import org.junit.Test;

import java.io.*;
import java.rmi.Naming;

/**
 * @author Jan Paw
 *         Date: 6/11/13
 */
@Deprecated
public class FileTransferTest {
    final public static int BUF_SIZE = 1024 * 64;

    public static void copy(java.io.InputStream in, java.io.OutputStream out)
            throws IOException {

        if (in instanceof InputStream) {
            System.out.println("Using Pipe of InputStream");
            ((InputStream) in).transfer(out);
            return;
        }

        if (out instanceof OutputStream) {
            System.out.println("Using Pipe of OutputStream");
            ((OutputStream) out).transfer(in);
            return;
        }
        System.out.println("Using byte[] read/write");
        byte[] b = new byte[BUF_SIZE];
        int len;
        while ((len = in.read(b)) >= 0) {
            out.write(b, 0, len);
        }
        in.close();
        out.close();
    }

    public static void upload(PeerService.IPeer server, File src,
                              File dest) throws IOException {
        copy(new FileInputStream(src),
                server.getOutputStream(dest));
    }

    public static void download(PeerService.IPeer server, File src,
                                File dest) throws IOException {
        copy(server.getInputStream(src),
                new FileOutputStream(dest));
    }

    //TODO create MD5 test
    @Test
    public static void main(String[] args) throws Exception {
        //FIRST RUN PeerService!!!
        String url = "rmi://localhost/peer";
        PeerService.IPeer server = (PeerService.IPeer) Naming.lookup(url);
        try {
            RandomAccessFile f = new RandomAccessFile("Test1GB.dat", "rw");
            f.setLength(1024 * 1024 * 1024);

        } catch (Exception e) {
            System.err.println(e);
        }

        File testFile = new File("Test1GB.dat");
        long len = testFile.length();

        long t;
        t = System.currentTimeMillis();
        download(server, testFile, new File("download.dat"));
        t = (System.currentTimeMillis() - t) / 1000;
        System.out.println("download: " + (len / t / 1000000d) +
                " MB/s");

        t = System.currentTimeMillis();
        upload(server, new File("download.tif"),
                new File("upload.dat"));
        t = (System.currentTimeMillis() - t) / 1000;
        System.out.println("upload: " + (len / t / 1000000d) +
                " MB/s");
    }
}
