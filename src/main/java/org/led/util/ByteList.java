package org.led.util;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Array backed implementation of a byte list. Shamelessly copying and adapting
 * from ArrayList.
 * 
 * 
 */
public class ByteList {
    private static final Logger LOG = LogManager.getLogger(ByteList.class);

    private static final int EIGHT_BYTES = 8;

    /**
     * The maximum size of array to allocate. Some VMs reserve some header words
     * in an array. Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private byte[] bytes;

    private int size;

    protected transient int modCount = 0;

    public ByteList() {
        this(EIGHT_BYTES);
    }

    public ByteList(int initialCapacity) {

        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "
                    + initialCapacity);
        this.bytes = new byte[initialCapacity];

    }

    /**
     * Returns the number of elements in this list.
     * 
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    public byte[] toArray() {
        byte[] result = new byte[size];
        System.arraycopy(bytes, 0, result, 0, size);
        return result;
    }

    public boolean add(byte b) {
        ensureCapacityInternal(size + 1); // Increments modCount!!
        bytes[size++] = b;
        return true;
    }

    public void add(int index, byte element) {
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1); // Increments modCount!!
        System.arraycopy(bytes, index, bytes, index + 1, size - index);
        bytes[index] = element;
        size++;
    }

    public boolean addAll(byte[] bytesToAdd) {

        int numNew = bytesToAdd.length;
        ensureCapacityInternal(size + numNew); // Increments modCount
        System.arraycopy(bytesToAdd, 0, bytes, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    private void ensureCapacityInternal(int minCapacity) {
        modCount++;
        // overflow-conscious code
        if (minCapacity - bytes.length > 0)
            grow(minCapacity);
    }

    /**
     * A version of rangeCheck used by add and addAll.
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message. Of the many
     * possible refactorings of the error handling code, this "outlining"
     * performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    /**
     * Increases the capacity to ensure that it can hold at least the number of
     * elements specified by the minimum capacity argument.
     * 
     * @param minCapacity
     *            the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = bytes.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        bytes = Arrays.copyOf(bytes, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE
                : MAX_ARRAY_SIZE;
    }

    /**
     * Converts the ByteList to a String. TODO Faster implementation.
     */
    @Override
    public String toString() {

        CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
        ByteBuffer buffer = ByteBuffer.wrap(toArray());
        try {
            return decoder.decode(buffer).toString();
        } catch (CharacterCodingException e) {
            LOG.error(e);
            return null;
        }
    }

}
