package org.led.http;

public interface FilterChain {

    void filter(HttpRequest request, HttpResponse response);

}
