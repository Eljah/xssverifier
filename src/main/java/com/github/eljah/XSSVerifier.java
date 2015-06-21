package com.github.eljah;

import com.github.eljah.utils.*;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ilya Evlampiev on 16.06.15.
 */
public class XSSVerifier {
    private static Map<Integer,XSSCheck> xssChecks = new HashMap<Integer,XSSCheck>();
    private static Map<Integer,SnifferService> snifferServices = new HashMap<Integer,SnifferService>();

    private static final Logger log = Logger.getLogger(XSSVerifier.class);

    public static XSSCheck createNewCheck(int port, String scriptTemplate, ResourceGenerator rg)
    {
        String resource=rg.getResource();
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
            snf.setResourceGenerator(rg);
            snifferServices.put(toAdd.getPort(),snf);
        }
        return toAdd;
    }

    public static void main(String[] args) throws InterruptedException {
        LimitedRandomImageResourceGenerator tg=new LimitedRandomImageResourceGenerator();
        tg.setStringLength(3);
        tg.setFileLocation("bug.jpg");
        tg.setExt("jpg");
        XSSCheck x=XSSVerifier.createNewCheck(4567, XSSTypes.JAVSCRIPT_IMG_TAG.toString(), tg);
        log.info(x.getXSS());
        while (!x.getXSSURLCalledStatus())
        {
            Thread.sleep(1000);
        }
        log.info("XSS URL requested");
        while (!x.getXSSCookiePassedStatus())
        {
            Thread.sleep(1000);
        }
        log.info("Cookie passed");
        while (!x.getXSSCookieHijacked("visited"))
        {
            Thread.sleep(1000);
        }
        log.info("Specific cookie `visited` was hijacked");
        while (!x.getXSSCookieHijacked("visited2"))
        {
            Thread.sleep(1000);
        }
        log.info("Specific cookie `visited2` was hijacked");
    }
}
