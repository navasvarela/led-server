package org.led.http.protocol;

public enum MimeTypes {
    PLAINTEXT("text/plain"), HTML("text/html"), DEFAULT_BINARY(
            "application/octet-stream"), XML("text/xml"), JSON(
            "application/json");
    private String mime;

    private MimeTypes(String type) {
        mime = type;
    }

    public String getMime() {
        return mime;
    }

}
