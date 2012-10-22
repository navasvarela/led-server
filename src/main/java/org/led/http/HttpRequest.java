package org.led.http;

import java.util.HashMap;
import java.util.Map;

import org.led.http.protocol.HttpMethod;

public class HttpRequest {

    private final HttpMethod method;

    private final String path;

    private final Map<String, String> headers;

    protected HttpRequest(HttpMethod theMethod, String thePath,
            Map<String, String> theHeaders) {

        method = theMethod;
        path = thePath;
        headers = theHeaders;
    }

    protected HttpRequest(HttpMethod theMethod, String thePath) {
        this(theMethod, thePath, new HashMap<String, String>());

    }

    protected void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

}
