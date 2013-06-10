import com.fileshare.communication.Node;
import com.fileshare.configuration.Policy;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Jan Paw
 *         Date: 6/10/13
 */
public class EchoTest {
    //[1v1] echo test - local -  issue#6
    @Test(timeout = 1024)
    public void oneToOneEcho() {
        NetworkService.boot(Policy.TEST);
        final String node1Name = "node1";
        final String node2Name = "node2";
        Node node1 = new Node(node1Name);
        Node node2 = new Node(node2Name);
        node1.connect(node2Name);
        assertTrue(node1.echo(node1Name).equals(node1Name));
    }
}
