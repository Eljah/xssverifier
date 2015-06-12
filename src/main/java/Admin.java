import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ilya Evlampiev on 08.06.15.
 */
public class Admin {
    private static final Logger log = Logger.getLogger(Admin.class);
    static Map<Integer, SnifferService> sniffersMap = new HashMap<Integer, SnifferService>();

    public void listenStart(int port) {
        try
        {
        SnifferService toBeAddded = new SnifferService(port);
        sniffersMap.put(port, toBeAddded);
        log.info("Sniffer service working on the port " + port + " is added to the map");
        }
        catch (Exception e)
        {
           if (e instanceof java.net.BindException)
           {
               log.error("The port "+port+" is already bent; please try another one");
           }
            log.error("Unknown exception\n"+e.getMessage());
        }

    }

    public void listenStop(int port) {
        sniffersMap.get(port).stop();
        sniffersMap.remove(port);
        log.info("Sniffer service working on the port " + port + " is removed from the map");
    }

    public void listenStop() {
        if (sniffersMap.size() == 1) {
            SnifferService snifferServiceToBeRemoved = sniffersMap.values().iterator().next();
            snifferServiceToBeRemoved.stop();
            sniffersMap.clear();
            log.info("Sniffer service is removed from the map");
        } else {
            log.error("The method shouldn't be called since there are more than one Sniffer service running");
        }
    }
}
