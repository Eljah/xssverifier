package com.github.eljah.utils;

/**
 * Created by Ilya Evlampiev on 21.06.15.
 */
public class LimitedRandomImageResourceGenerator extends LimitedRandomResourceGenerator {
    String ext;

    public void setExt(String ext) {
        this.ext = ext;
    }

    @Override
    public String getResource() {
        return resource+"."+ext;
    }

}
