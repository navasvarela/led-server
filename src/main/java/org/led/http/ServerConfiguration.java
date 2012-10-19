package org.led.http;

import java.util.Properties;

public class ServerConfiguration {

    private static final String SERVER_PORT_PROP = "led.server.port";
    private static final String SERVER_THREADS_PROP = "led.server.threads";

    private static final String DEFAULT_PORT = "8888";
    private static final String DEFAULT_THREADS = "10";
    private final int port;

    public ServerConfiguration(Properties properties) {

        port = Integer.parseInt(properties.getProperty(SERVER_PORT_PROP,
                DEFAULT_PORT));

    }

    public ServerConfiguration(int thePort) {

        port = thePort;

    }

    public int getPort() {
        return port;
    }

}
