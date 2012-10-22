package org.led.http;

import java.util.Properties;

public class ServerConfiguration {

    private static final String SERVER_PORT_PROP = "led.server.port";
    private static final String SERVER_THREADS_PROP = "led.server.threads";

    private static final String DEFAULT_PORT = "8888";
    private static final String DEFAULT_THREADS = "100";
    private final int port;
    private final int threads;

    public ServerConfiguration(Properties properties) {

        port = Integer.parseInt(properties.getProperty(SERVER_PORT_PROP,
                DEFAULT_PORT));
        threads = Integer.parseInt(properties.getProperty(SERVER_THREADS_PROP,
                DEFAULT_THREADS));

    }

    public ServerConfiguration(int thePort, int theThreads) {

        port = thePort;
        threads = theThreads;

    }

    public int getPort() {
        return port;
    }

    public int getThreads() {
        return threads;
    }

}
