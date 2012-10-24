package org.led.http;

import java.util.Properties;

public class ServerConfiguration {

    private static final String SERVER_PORT_PROP = "led.server.port";
    private static final String SERVER_THREADS_PROP = "led.server.threads";

    private static final String SERVER_MAX_PROP = "led.server.max.post.mb";
    private static final String DEFAULT_PORT = "8888";
    private static final String DEFAULT_THREADS = "100";
    private static final String DEFAULT_MAX = "20";
    private final int port;
    private final int threads;
    private final int maxPostMB;

    public ServerConfiguration(Properties properties) {

        port = Integer.parseInt(properties.getProperty(SERVER_PORT_PROP,
                DEFAULT_PORT));
        threads = Integer.parseInt(properties.getProperty(SERVER_THREADS_PROP,
                DEFAULT_THREADS));
        maxPostMB = Integer.parseInt(properties.getProperty(SERVER_MAX_PROP,
                DEFAULT_MAX));

    }

    public ServerConfiguration(int thePort, int theThreads, int theMaxPostMB) {
        maxPostMB = theMaxPostMB;
        port = thePort;
        threads = theThreads;

    }

    public int getPort() {
        return port;
    }

    public int getThreads() {
        return threads;
    }

    public int getMaxPostMB() {
        return maxPostMB;
    }

}
