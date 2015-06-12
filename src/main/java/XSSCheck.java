/**
 * Created by Ilya Evlampiev on 13.06.15.
 */
public class XSSCheck {
    private String host;
    private int port;
    private String resource;

    XSSCheck(String host, int port, String resource)
    {
        this.host=host;
        this.port=port;
        this.resource=resource;
    }
}
