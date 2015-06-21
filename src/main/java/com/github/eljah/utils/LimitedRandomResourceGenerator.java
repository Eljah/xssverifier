package com.github.eljah.utils;

import fi.iki.elonen.NanoHTTPD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Created by Ilya Evlampiev on 21.06.15.
 */
public class LimitedRandomResourceGenerator implements ResourceGenerator {
    public String resource;
    String fileLocation;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();

    static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public void setStringLength(int length) {
        resource = randomString(length);
    }

    @Override
    public String getResource() {
        return resource;
    }

    public void setFileLocation(String location)
    {
        fileLocation=location;
    }

    @Override
    public NanoHTTPD.Response responceOfResource() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileLocation);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, "image/jpeg", fis);
    }
}
