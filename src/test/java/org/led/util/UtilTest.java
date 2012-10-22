package org.led.util;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.Test;

public class UtilTest {

    private static final String FIRST_LINE = "This is the first line\n";
    private static final String LINE = FIRST_LINE + "This is the second line";

    @Test
    public void shouldFindLineInDefaultCharset() {
        // setup
        ByteBuffer buffer = ByteBuffer.wrap(LINE.getBytes());
        // act
        long time = System.currentTimeMillis();
        String firstLine = Util.readLine(buffer);
        System.err.println("LASTED: " + (System.currentTimeMillis() - time));
        // assert
        System.err.println("READ:       "
                + Util.bytesToHex(firstLine.getBytes()));
        System.err.println("FIRST LINE: "

        + Util.bytesToHex(FIRST_LINE.getBytes()));
        assertEquals(FIRST_LINE, firstLine);
    }

    @Test
    public void shouldFindLineInUTF8() throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(LINE.getBytes("UTF-8"));
        // act
        long time = System.currentTimeMillis();
        String firstLine = Util.readLine(buffer);
        System.err.println("LASTED: " + (System.currentTimeMillis() - time));
        // assert
        System.err.println("READ:       "
                + Util.bytesToHex(firstLine.getBytes("UTF-8")));
        System.err.println("FIRST LINE: "
                + Util.bytesToHex(FIRST_LINE.getBytes("UTF-8")));
        assertEquals(FIRST_LINE, firstLine);
    }
}
