package com.github.eljah.utils;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Ilya Evlampiev on 21.06.15.
 */
public interface  ResourceGenerator {
   String getResource();

    NanoHTTPD.Response responceOfResource();
}
