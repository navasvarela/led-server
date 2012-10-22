package org.led.http.protocol;

public enum HttpMethod {
    DELETE("DELETE"), GET("GET"), HEAD("HEAD"), POST("POST"), PUT("PUT")
    /* pathological */
    , CONNECT("CONNECT"), OPTIONS("OPTIONS"), TRACE("TRACE")
    /* webdav */
    , COPY("COPY"), LOCK("LOCK"), MKCOL("MKCOL"), MOVE("MOVE"), PROPFIND(
            "PROPFIND"), PROPPATCH("PROPPATCH"), UNLOCK("UNLOCK"), REPORT(
            "REPORT"), MKACTIVITY("MKACTIVITY"), CHECKOUT("CHECKOUT"), MERGE(
            "MERGE"), MSEARCH("M-SEARCH"), NOTIFY("NOTIFY"), SUBSCRIBE(
            "SUBSCRIBE"), UNSUBSCRIBE("UNSUBSCRIBE"), PATCH("PATCH"), PURGE(
            "PURGE");

    private String name;

    private HttpMethod(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }

}
