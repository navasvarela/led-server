package org.led.http;

import java.util.HashMap;
import java.util.Map;

import org.led.http.protocol.HttpMethod;

public class HttpRequest {

    private static final int SMALL_BODY_LIMIT = 128;

    private final HttpMethod method;

    private final String path;

    private final Map<String, String> headers;

    private byte[] body;

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

    protected void setBody(byte[] theBody) {
        body = theBody;
    }

    public byte[] getBody() {
        return body;
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

    @Override
    public String toString() {
        String string = "HttpRequest [method=" + method + ", path=" + path
                + ", headers=" + headers;
        if (body != null && body.length < 128) {
            string += ", body=" + new String(body);
        }
        return string + "]";
    }

}
