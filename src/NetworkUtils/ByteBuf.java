package NetworkUtils;

import java.util.Arrays;
import java.util.Collections;

public class ByteBuf {
    private byte[] buf;
    float expandSize = 2f;
    boolean writeable = true;
    private int readerIndex = 0;
    private int writtenBytes = 0;

    public ByteBuf() {
        buf = new byte[2];
    }

    public ByteBuf(byte[] buf) {
        this.buf = buf;
    }

    public ByteBuf(int size) {
        this.buf = new byte[size];
    }

    public ByteBuf(ByteBuf buf, int start, int length) {
        this(buf.buf, start, length);
    }

    public ByteBuf(byte[] buf, int start, int length) {
        if (length < 0) throw new IllegalArgumentException("Value 0 for length given!");
        this.buf = Arrays.copyOfRange(buf, start, start + length);
        this.writeable = false;
    }


    // This may cause issues if it expands while a sub array exists
    // Perhaps make sub array copy by value

    private void expand() {
        byte[] temp = new byte[(int) (buf.length * expandSize + 1)];
        System.arraycopy(buf,0, temp, 0, buf.length);
        this.buf = temp;
    }

    public void readerIndex(int i) {
        if (readerIndex < 0) return;
        this.readerIndex = i;
    }

    public byte[] getRawBytes() {
        return Arrays.copyOf(buf, writtenBytes); // Return the written portion of the buffer
    }

    public int readerIndex() {
        return this.readerIndex;
    }

    public boolean isReaderIndexLast() {
        return readerIndex == writtenBytes;
    }

    private boolean expandIfNeeded(int byteSize) {
        if (buf.length - writtenBytes < byteSize) {
            expand();
            return true;
        }
        return false;
    }

    public void writeByte(byte b) {
        if (!writeable) throw new IllegalStateException("Attempted to write to non writeable byte buf!");
        if (expandIfNeeded(1)) {
            writeByte(b);
            return;
        }
        if (isReaderIndexLast()) {
            this.buf[readerIndex] = b;
        } else {
            byte temp;
            for (int i = readerIndex; i <= writtenBytes; i++) { // Not sure if this is right
                temp = this.buf[i];
                this.buf[i] = b;
                b = temp;
            }
        }
        readerIndex ++;
        writtenBytes ++;
    }

    public void writeInt(int i) {
        if (!writeable) throw new IllegalStateException("Attempted to write to non writeable byte buf!");
        writeByte((byte) (i >> 24));
        writeByte((byte) ((i & 0xFF0000) >> 16));
        writeByte((byte) ((i & 0xFF00) >> 8));
        writeByte((byte) (i & 0xFF));
    }


    public byte readByte() {
        if (readerIndex >= writtenBytes) {
            throw new IndexOutOfBoundsException("Attempted to read beyond buffer limit!");
        }
        return buf[readerIndex++]; // Return byte and move reader index forward
    }

    // read int
    // write bytes
    // read bytes

    // write string
    // read string
}
