import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by Ilya Evlampiev on 08.06.15.
 */
public class AdminService extends NanoHTTPD {
    private static final Logger log = Logger.getLogger(AdminService.class);
    private static Admin admin= new Admin();

    public AdminService() {
        super(8085);
    }

    public AdminService(int port) {
        super(port);
    }

    @Override public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        log.info("Admin received "+method + " '" + uri + "' ");

        if (uri.equals("/start"))
        {
            Map<String, String> parms = session.getParms();
            try
            {
            admin.listenStart(Integer.parseInt(parms.get("port")));
            }
            catch (NullPointerException e)
            {
                log.error("Parameter port was not passed to the admin service");
            }
            catch (NumberFormatException e)
            {
                log.error("Parameter port has wrong non-integer format");
            }
        }
        if (uri.equals("/stop"))
        {
            Map<String, String> parms = session.getParms();
            try
            {
                admin.listenStop(Integer.parseInt(parms.get("port")));
            }
            catch (NullPointerException e)
            {
                log.error("Parameter port was not passed to the admin service; if the only Sniffer service is running it will be stopped");
                admin.listenStop();
            }
            catch (NumberFormatException e)
            {
                log.error("Parameter port has wrong non-integer format");
            }
        }


        String msg =
                "<form action='?' method='get'>\n" +
                        "  <p>Your name: <input type='text' name='port'></p>\n" +
                        "</form>\n";

        return new NanoHTTPD.Response(msg);
    }


    public static void main(String[] args) {
        ServerRunner.run(AdminService.class);
    }
}
