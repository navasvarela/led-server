package org.led.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.led.http.protocol.HttpStatus;

public class HttpProtocolHandler implements
        CompletionHandler<AsynchronousSocketChannel, Void> {

    private static final String HTTP_1_1 = "HTTP/1.1 ";
    private static final Logger LOG = LogManager
            .getLogger(HttpProtocolHandler.class);
    private final Charset UTF_8 = Charset.forName("UTF-8");
    private final CharsetEncoder encoder = UTF_8.newEncoder();
    private final CharsetDecoder decoder = UTF_8.newDecoder();

    private static final int BUFFER_SIZE = 4 * 1024;

    private final AsynchronousServerSocketChannel listener;

    public HttpProtocolHandler(AsynchronousServerSocketChannel theListener) {
        listener = theListener;
    }

    @Override
    public void completed(AsynchronousSocketChannel result, Void theAttachment) {
        // process next request
        LOG.debug("Completed");
        listener.accept(null, new HttpProtocolHandler(listener));
        handle(result);

    }

    @Override
    public void failed(Throwable theExc, Void theAttachment) {
        // TODO Auto-generated method stub
        LOG.debug("failed", theExc);
    }

    private void handle(AsynchronousSocketChannel channel) {
        // Create a direct buffer to get bytes from socket.
        // Direct buffers should be long-lived and be reused as much as
        // possible.
        LOG.debug("Handling message");
        ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        String message = "";

        try {
            // Clear the buffer and read bytes from socket
            buf.clear();
            int bytesRead;
            do {
                bytesRead = channel.read(buf).get();
                if (bytesRead != -1) {
                    buf.flip();
                    // Read the bytes from the buffer ...;
                    LOG.debug("Reading...");
                    message += readFromByteBuffer(buf);
                    buf.clear();

                }
            } while (bytesRead != -1 && bytesRead == BUFFER_SIZE);

            LOG.debug("REQUEST: " + message);
            channel.write(toByteBuffer(HTTP_1_1 + HttpStatus.HTTP_OK));
        } catch (ExecutionException | InterruptedException
                | CharacterCodingException e) {
            LOG.error(e);
            try {
                channel.write(toByteBuffer("HTTP 1.1 "
                        + HttpStatus.HTTP_INTERNALERROR));
            } catch (CharacterCodingException e1) {
                LOG.error("Cannot Send Response: " + e1.getMessage(), e1);
            }
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
            } finally {

                try {
                    channel.close();
                } catch (IOException e1) {

                }

            }
        }
    }

    private String readFromByteBuffer(ByteBuffer buf)
            throws CharacterCodingException {
        LOG.debug("readFromByteBuffer");
        decoder.reset();
        return decoder.decode(buf).toString();
    }

    private ByteBuffer toByteBuffer(String message)
            throws CharacterCodingException {
        if (LOG.isDebugEnabled())
            LOG.debug("toByteBuffer: " + message);
        encoder.reset();
        return encoder.encode(CharBuffer.wrap(message));
    }

}
