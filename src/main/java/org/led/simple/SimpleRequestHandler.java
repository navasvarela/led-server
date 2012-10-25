package org.led.simple;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.led.http.HttpRequest;
import org.led.http.HttpResponse;
import org.led.http.RequestHandler;
import org.led.http.RequestHandlerFactory;
import org.led.http.protocol.HttpMethod;
import org.led.http.protocol.HttpStatus;

/**
 * Example request handler. Serves local files.
 */
public class SimpleRequestHandler implements RequestHandler,
        RequestHandlerFactory {

    private static final Logger LOG = LogManager
            .getLogger(SimpleRequestHandler.class);
    private static final int CAPACITY = 16 * 1024;

    private final String baseDir;

    private final SimpleRequestHandler instance;

    public SimpleRequestHandler(String dir) {
        baseDir = dir;
        instance = this;
    }

    public SimpleRequestHandler() {
        // Sets the base dir to the current execution folder
        baseDir = System.getProperty("user.dir");
        instance = this;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        if (request.getMethod() != HttpMethod.GET) {
            // TODO Send Bad Request error
            return;
        } else {
            String filename = baseDir + request.getPath();

            RandomAccessFile file;
            try {
                file = new RandomAccessFile(filename, "r");
            } catch (FileNotFoundException e) {
                response.sendStatus(HttpStatus.NOTFOUND);
                return;

            }
            response.sendStatus(HttpStatus.OK);
            FileChannel src = file.getChannel();
            final ByteBuffer buffer = ByteBuffer.allocateDirect(CAPACITY);
            try {
                while (src.read(buffer) != -1) {
                    // prepare the buffer to be drained
                    buffer.flip();
                    // write to the channel, may block
                    response.write(buffer);
                    // If partial transfer, shift remainder down
                    // If buffer is empty, same as doing clear()
                    buffer.compact();
                }
                // EOF will leave buffer in fill state
                buffer.flip();
                // make sure the buffer is fully drained.
                while (buffer.hasRemaining()) {
                    response.write(buffer);
                }
            } catch (InterruptedException | IOException | ExecutionException e) {
                // TODO Send 500 error and log cause
                response.sendStatus(HttpStatus.INTERNALERROR);
                LOG.error(e);
            }
        }
    }

    @Override
    public RequestHandler newRequestHandler(HttpRequest theRequest) {
        return instance;
    }

}
