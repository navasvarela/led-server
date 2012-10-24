package org.led.simple;

import org.led.http.HttpRequest;
import org.led.http.HttpResponse;
import org.led.http.RequestHandler;

/**
 * Example request handler. Serves local files.
 */
public class SimpleRequestHandler implements RequestHandler {

    private final String baseDir;

    public SimpleRequestHandler(String dir) {
        baseDir = dir;

    }

    public SimpleRequestHandler() {
        // Sets the base dir to the current execution folder
        baseDir = System.getProperty("user.dir");
    }

    @Override
    public void handle(HttpRequest theRequest, HttpResponse theResponse) {

    }

}
