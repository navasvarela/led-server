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

    @Test
    public void shouldParseHeader() throws Exception {
        ByteBuffer buf = ByteBuffer.wrap("Content-Type: text/html\r\n"
                .getBytes());
        // act
        String[] header = Parser.parseHeaderLine(buf);
        // assert
        assertEquals("Content-Type", header[0]);
        assertEquals("text/html", header[1]);
    }

    @Test
    public void shouldFindEmptyLine() {
        ByteBuffer buf = ByteBuffer.wrap("\r\n".getBytes());
        // act
        String[] header = Parser.parseHeaderLine(buf);
        // assert
        assertEquals("", header[0]);
        assertEquals("", header[1]);
    }
}
