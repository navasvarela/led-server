package org.led.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.ByteBuffer;

import org.junit.Test;
import org.led.http.protocol.HttpMethod;

public class ParserTest {

    @Test
    public void shouldParseRequestLine() {
        // setup
        ByteBuffer buf = ByteBuffer.wrap("GET /test HTTP/1.1\n".getBytes());
        // act
        HttpRequest request = Parser.parseRequestLine(buf);
        // assert
        assertNotNull(request);
        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/test", request.getPath());
    }

}
