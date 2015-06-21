package com.github.eljah.utils;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Ilya Evlampiev on 21.06.15.
 */
public class TrivialResourceGenerator implements ResourceGenerator {
    String resource;
    String resourceHtml;

    public void setResource(String resource)
    {
        this.resource=resource;
    }

    @Override
    public String getResource() {
        return resource;
    }

    public void setResourceValue(String html)
    {
        this.resourceHtml=html;
    }

    @Override
    public NanoHTTPD.Response responceOfResource() {
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, "text/html", resourceHtml);
           }
}
