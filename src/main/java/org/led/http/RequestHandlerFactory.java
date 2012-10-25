package org.led.http;

/**
 * Thread-safe RequestHandler factory object.
 * 
 * 
 */
public interface RequestHandlerFactory {

    public RequestHandler newRequestHandler(HttpRequest request);

}
