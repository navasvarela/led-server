package org.led.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class Server {

    private final ServerConfiguration configuration;

    private Server(ServerConfiguration theConfiguration) {

        configuration = theConfiguration;
    }

    public void start() throws IOException {
        final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel
                .open().bind(new InetSocketAddress(configuration.getPort()));

        serverSocketChannel.accept(null, new HttpProtocolHandler(
                serverSocketChannel));
    }
}
