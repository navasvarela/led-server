package org.led.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
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

        try {
            // Clear the buffer and read bytes from socket
            buf.clear();
            int bytesRead;

            bytesRead = channel.read(buf).get();

            if (bytesRead != -1) {
                buf.flip();
                // Read the bytes from the buffer ...;
                // 1. Read Request Line and get Request Object
                HttpRequest request = Parser.parseRequestLine(buf);
                // TODO 2. Read Content Headers
                String[] header = new String[2];
                while ((header = Parser.parseHeaderLine(buf)) != null
                        && !"".equalsIgnoreCase(header[0])) {

                    request.addHeader(header[0], header[1]);

                }
                // TODO 3. Read body (what about multipart?)
                // TODO 4. Apply Filters
                // TODO 5. Invoke RequestHandler/Router

                buf.clear();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("REQUEST: " + request);
                }

            }
            // Read the rest of the request.
            channel.write(toByteBuffer(HTTP_1_1 + HttpStatus.OK));
        } catch (ExecutionException | InterruptedException
                | CharacterCodingException e) {
            LOG.error(e);
            try {
                channel.write(toByteBuffer("HTTP 1.1 "
                        + HttpStatus.INTERNALERROR));
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

    // We'll use this for the response.
    private ByteBuffer toByteBuffer(String message)
            throws CharacterCodingException {
        if (LOG.isDebugEnabled())
            LOG.debug("toByteBuffer: " + message);
        encoder.reset();
        return encoder.encode(CharBuffer.wrap(message));
    }

}
