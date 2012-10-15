package org.led.http;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class HttpProtocolHandler implements
        CompletionHandler<AsynchronousSocketChannel, Void> {

    private final AsynchronousServerSocketChannel listener;

    public HttpProtocolHandler(AsynchronousServerSocketChannel theListener) {
        listener = theListener;
    }

    @Override
    public void completed(AsynchronousSocketChannel result, Void theAttachment) {
        // process next request
        listener.accept(null, this);
        handle(result);

    }

    @Override
    public void failed(Throwable theExc, Void theAttachment) {
        // TODO Auto-generated method stub

    }

    private void handle(AsynchronousSocketChannel theResult) {
        // TODO Auto-generated method stub

    }

}
