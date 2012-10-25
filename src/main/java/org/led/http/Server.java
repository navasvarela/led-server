package org.led.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {

    private static final Logger LOG = LogManager.getLogger(Server.class);

    private final ServerConfiguration configuration;

    private AsynchronousChannelGroup group;

    private boolean started;

    public Server(ServerConfiguration theConfiguration) {

        configuration = theConfiguration;
    }

    public void start() throws IOException {
        LOG.debug("Starting LED Server on Port: " + configuration.getPort());
        group = AsynchronousChannelGroup.withCachedThreadPool(
                Executors.newCachedThreadPool(), configuration.getThreads());
        final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel
                .open(group).bind(
                        new InetSocketAddress(configuration.getPort()));
        serverSocketChannel
                .setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
        serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

        serverSocketChannel.accept(null, new HttpProtocolHandler(
                serverSocketChannel, configuration.getHandlerFactory(),
                configuration.getMaxPostMB()));
        started = true;
        LOG.debug("Server started successfully");
        try {
            group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            LOG.info("Terminating ");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    public void stop() {
        if (group != null) {
            LOG.info("Shutting down LED server...");
            group.shutdown();
        }
    }

    public boolean isStarted() {
        return started;
    }

}
