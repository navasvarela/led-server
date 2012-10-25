package org.led.http;

import java.util.Properties;

public class ServerConfiguration {

    private static final String SERVER_PORT_PROP = "led.server.port";
    private static final String SERVER_THREADS_PROP = "led.server.threads";

    private static final String SERVER_MAX_PROP = "led.server.max.post.mb";
    private static final String FACTORY_CLASS_PROP = "led.server.handler.factory";
    private static final String DEFAULT_PORT = "8888";
    private static final String DEFAULT_THREADS = "100";
    private static final String DEFAULT_MAX = "20";
    private static final String DEFAULT_CLASS = "org.led.simple.SimpleRequestHandler";
    private final int port;
    private final int threads;
    private final int maxPostMB;
    private final RequestHandlerFactory handlerFactory;

    public ServerConfiguration(Properties properties) {

        port = Integer.parseInt(properties.getProperty(SERVER_PORT_PROP,
                DEFAULT_PORT));
        threads = Integer.parseInt(properties.getProperty(SERVER_THREADS_PROP,
                DEFAULT_THREADS));
        maxPostMB = Integer.parseInt(properties.getProperty(SERVER_MAX_PROP,
                DEFAULT_MAX));

        String factoryClass = properties.getProperty(FACTORY_CLASS_PROP,
                DEFAULT_CLASS);

        try {
            handlerFactory = (RequestHandlerFactory) Class
                    .forName(factoryClass).newInstance();
        } catch (InstantiationException | IllegalAccessException
                | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public ServerConfiguration(int thePort, int theThreads, int theMaxPostMB,
            RequestHandlerFactory factory) {
        maxPostMB = theMaxPostMB;
        port = thePort;
        threads = theThreads;
        handlerFactory = factory;

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

    public RequestHandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    @Override
    public String toString() {
        return "ServerConfiguration [port=" + port + ", threads=" + threads
                + ", maxPostMB=" + maxPostMB + ", handlerFactory="
                + handlerFactory + "]";
    }

}
