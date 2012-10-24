package org.led.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class ChannelUtilsTest {

    private static final String HELLO_WORLD = "Hello world";
    @Mock
    private AsynchronousSocketChannel channel;

    @Test
    public void shouldReadFully() throws Exception {
        final byte[] bytes = HELLO_WORLD.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        final Future<Integer> future = mock(Future.class);
        when(channel.read(buffer)).thenAnswer(new Answer<Future<Integer>>() {

            @Override
            public Future<Integer> answer(InvocationOnMock theInvocation)
                    throws Throwable {
                ByteBuffer buf = (ByteBuffer) theInvocation.getArguments()[0];
                buf.clear();
                buf.put(bytes);
                return future;
            }
        });
        when(future.get()).thenReturn(1);
        // act
        byte[] bytesRead = ChannelUtils.readFully(buffer, channel,
                2 * bytes.length);
        // assert
        assertEquals(HELLO_WORLD + HELLO_WORLD, new String(bytesRead));

    }

    @Test
    public void bufferCapacityTest() {

        byte[] bytes = HELLO_WORLD.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int beforeCapacity = buffer.capacity();
        buffer.get();
        int afterCapacity = buffer.capacity();
        assertEquals(bytes.length, beforeCapacity);
        assertEquals(beforeCapacity, afterCapacity);
    }

}
