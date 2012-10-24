package org.led.http;

public interface Filter {

    void filter(HttpRequest request, HttpResponse response, FilterChain chain);

}
