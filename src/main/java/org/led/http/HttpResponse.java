package org.led.http;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.led.http.protocol.HttpStatus;

public class HttpResponse {

    private static final String HTTP_1_1 = "HTTP/1.1 ";
    private final Map<String, String> headers;

    private HttpStatus status;

    private final AsynchronousSocketChannel channel;

    protected HttpResponse(Map<String, String> theHeaders,
            AsynchronousSocketChannel theChannel) {
        headers = theHeaders;
        status = HttpStatus.OK;
        channel = theChannel;
    }

    protected HttpResponse(AsynchronousSocketChannel theChannel) {
        headers = new HashMap<String, String>();
        status = HttpStatus.OK;
        channel = theChannel;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus theStatus) {
        status = theStatus;
    }

    /**
     * Synchronous write operation.
     * 
     * @param buffer
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public int write(ByteBuffer buffer) throws InterruptedException,
            ExecutionException {
        return channel.write(buffer).get();
    }

    public void write(ByteBuffer buffer,
            CompletionHandler<Integer, Void> handler) {
        channel.write(buffer, null, handler);
    }

    // TODO Send Error
    public void sendStatus(HttpStatus status) {
        channel.write(ByteBuffer.wrap((HTTP_1_1 + status).getBytes()));
    }
    // TODO Send Redirect

}
