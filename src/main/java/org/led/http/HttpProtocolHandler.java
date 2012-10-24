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
import org.led.util.ChannelUtils;

public class HttpProtocolHandler implements
        CompletionHandler<AsynchronousSocketChannel, Void> {

    private static final String HTTP_1_1 = "HTTP/1.1 ";
    private static final Logger LOG = LogManager
            .getLogger(HttpProtocolHandler.class);
    private final Charset UTF_8 = Charset.forName("UTF-8");
    private final CharsetEncoder encoder = UTF_8.newEncoder();
    private final int maxPostMB;

    private static final int BUFFER_SIZE = 4 * 1024;

    private final AsynchronousServerSocketChannel listener;

    public HttpProtocolHandler(AsynchronousServerSocketChannel theListener,
            int max) {
        listener = theListener;
        maxPostMB = max * 1024 * 1024;
    }

    @Override
    public void completed(AsynchronousSocketChannel result, Void theAttachment) {
        // process next request
        LOG.debug("Completed");
        listener.accept(null, new HttpProtocolHandler(listener, maxPostMB));
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
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        try {
            // Clear the buffer and read bytes from socket
            buffer.clear();
            int bytesRead;

            bytesRead = channel.read(buffer).get();

            if (bytesRead != -1) {
                buffer.flip();
                // Read the bytes from the buffer ...;
                // 1. Read Request Line and get Request Object
                HttpRequest request = Parser.parseRequestLine(buffer);
                // TODO 2. Read Content Headers
                String[] header = new String[2];
                while ((header = Parser.parseHeaderLine(buffer)) != null
                        && !"".equalsIgnoreCase(header[0])) {
                    request.addHeader(header[0], header[1]);
                }
                // 3. Read body fully (what about multipart?)
                byte[] bodyBytes = ChannelUtils.readFully(buffer, channel,
                        maxPostMB);
                request.setBody(bodyBytes);
                // TODO 4. Apply Filters
                // TODO 5. Invoke RequestHandler/Router

                buffer.clear();

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
