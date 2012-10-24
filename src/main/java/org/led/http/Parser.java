package org.led.http;

import java.nio.ByteBuffer;

import org.led.http.protocol.HttpMethod;
import org.led.util.ByteList;

public final class Parser {
    public static final byte A = 0x41;
    public static final byte B = 0x42;
    public static final byte C = 0x43;
    public static final byte D = 0x44;
    public static final byte E = 0x45;
    public static final byte F = 0x46;
    public static final byte G = 0x47;
    public static final byte H = 0x48;
    public static final byte I = 0x49;
    public static final byte J = 0x4a;
    public static final byte K = 0x4b;
    public static final byte L = 0x4c;
    public static final byte M = 0x4d;
    public static final byte N = 0x4e;
    public static final byte O = 0x4f;
    public static final byte P = 0x50;
    public static final byte Q = 0x51;
    public static final byte R = 0x52;
    public static final byte S = 0x53;
    public static final byte T = 0x54;
    public static final byte U = 0x55;
    public static final byte V = 0x56;
    public static final byte W = 0x57;
    public static final byte X = 0x58;
    public static final byte Y = 0x59;
    public static final byte Z = 0x5a;
    public static final byte UNDER = 0x5f;
    public static final byte CR = 0x0d;
    public static final byte LF = 0x0a;
    public static final byte DOT = 0x2e;
    public static final byte SPACE = 0x20;
    public static final byte TAB = 0x09;
    public static final byte SEMI = 0x3b;
    public static final byte COLON = 0x3a;
    public static final byte HASH = 0x23;
    public static final byte QMARK = 0x3f;
    public static final byte SLASH = 0x2f;
    public static final byte DASH = 0x2d;
    public static final byte STAR = 0x2a;
    public static final byte NULL = 0x00;

    protected static HttpRequest parseRequestLine(ByteBuffer buffer) {
        // read first word
        ByteList bl = readWord(buffer, SPACE);
        HttpMethod method = HttpMethod.valueOf(new String(bl.toArray()));

        // Now read the request path
        bl = readWord(buffer, SPACE);
        String path = new String(bl.toArray());
        // read rest of the line
        readRestOfLine(buffer);

        return new HttpRequest(method, path);
    }

    protected static String[] parseHeaderLine(final ByteBuffer buffer) {
        ByteList bl = readWord(buffer, COLON);
        String[] header = new String[2];
        header[0] = new String(bl.toArray()).trim();
        bl = readWord(buffer, LF);
        header[1] = new String(bl.toArray()).trim();

        return header;
    }

    protected static ByteList readWord(ByteBuffer buffer, byte separator) {
        ByteList bl = new ByteList();
        byte sep = separator == 0 ? SPACE : separator;
        byte b = 0;
        while ((b = buffer.get()) != separator && b != CR) {
            bl.add(b);
        }
        return bl;
    }

    protected static void readRestOfLine(ByteBuffer buffer) {
        byte b = 0;
        while ((b = buffer.get()) != LF) {

        }
    }
}
