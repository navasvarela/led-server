package org.led.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.ExecutionException;

public class ChannelUtils {

    public static void copy(final ReadableByteChannel src,
            final WritableByteChannel dest) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (src.read(buffer) != -1) {
            // prepare the buffer to be drained
            buffer.flip();
            // write to the channel, may block
            dest.write(buffer);
            // If partial transfer, shift remainder down
            // If buffer is empty, same as doing clear()
            buffer.compact();
        }
        // EOF will leave buffer in fill state
        buffer.flip();
        // make sure the buffer is fully drained.
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }

    /**
     * Reads the bytebuffer first, then the channel fully.
     * 
     * @param buffer
     * @param channel
     * @param maxBytes
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static byte[] readFully(ByteBuffer buffer,
            AsynchronousSocketChannel channel, int maxBytes)
            throws InterruptedException, ExecutionException {
        if (buffer == null) {
            throw new IllegalArgumentException("Buffer cannot be null");
        }

        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }
        ByteList result = new ByteList();
        byte[] remainingBytes = new byte[buffer.remaining()];
        buffer.get(remainingBytes);
        result.addAll(remainingBytes);
        byte[] bytes = new byte[buffer.capacity()];
        while ((channel.read(buffer).get()) > 0 && result.size() < maxBytes) {
            buffer.flip();
            if (buffer.capacity() < bytes.length) {
                bytes = new byte[buffer.capacity()];
            }
            buffer.get(bytes);
            result.addAll(bytes);
            buffer.clear();
        }

        return result.toArray();
    }
}
