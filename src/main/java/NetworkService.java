import com.fileshare.communication.Node;
import com.fileshare.configuration.Policy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author Jan Paw
 *         Date: 6/10/13
 */
@Deprecated
public class NetworkService {
    private static final Logger logger = LogManager.getLogger(Node.class.getName());

    //JAR main executor
    public static void main(String args[]) {
        boot(Policy.JAR);
        String name;
        if (args.length > 0)
            name = args[0];
        else
            name = "Node";
        new Node(name);
    }

    public static void boot(Policy policy) {
        setPolicy(policy);
        setSecurityManager();
        setRegistry();
    }

    private static void setPolicy(Policy policy) {
        logger.trace("Policy setting!");
        System.setProperty("java.security.policy", policy.url());
        logger.info("Set policy: " + System.getProperty("java.security.policy"));
    }

    private static void setSecurityManager() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
            logger.trace("Security manager installed.");
        } else {
            logger.trace("Security manager already exists.");
        }
    }

    private static void setRegistry() {
        try {
            LocateRegistry.createRegistry(1099);
            logger.trace("java RMI registry created.");
        } catch (RemoteException e) {
            logger.trace("java RMI registry already exists.");
        }
    }
}
