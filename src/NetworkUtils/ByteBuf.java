package NetworkUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteBuf {
    private byte[] buf;
    float expandSize = 2f;
    boolean writeable = true;
    private int readerIndex = 0;
    private int writerIndex = 0;
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
        if (length < 0) throw new IllegalArgumentException("Value less than 0 for given for length!");
        this.buf = Arrays.copyOfRange(buf, start, start + length);
        this.writeable = false;
        this.writerIndex = length;
        this.writtenBytes = length;
    }


    // This may cause issues if it expands while a sub array exists
    // Perhaps make sub array copy by value

    private void expand() {
        byte[] temp = new byte[(int) (buf.length * expandSize + 1)];
        System.arraycopy(buf,0, temp, 0, buf.length);
        this.buf = temp;
    }

    public void readerIndex(int i) {
        if (i < 0) return;
        this.readerIndex = i;
    }

    public byte[] getRawBytes() {
        return Arrays.copyOf(buf, writtenBytes); // Return the written portion of the buffer
    }

    public int readerIndex() {
        return this.readerIndex;
    }

    public void writerIndex(int i) {
        if (i < 0) return;
        this.writerIndex = i;
    }

    public int writerIndex() {
        return this.writerIndex;
    }

    public boolean isReaderIndexLast() {
        return readerIndex == writtenBytes;
    }

    public boolean isWriterIndexLast() {
        return writerIndex == writtenBytes;
    }

    public int readableBytes() {
        return writtenBytes - this.readerIndex;
    }

    public int getWrittenByteCount() {
        return writtenBytes;
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
        if (isWriterIndexLast()) {
            this.buf[writerIndex] = b;
        } else {
            byte temp;
            for (int i = writerIndex; i <= writtenBytes; i++) { // Not sure if this is right
                temp = this.buf[i];
                this.buf[i] = b;
                b = temp;
            }
        }
        writerIndex ++;
        writtenBytes ++;
    }

    public void writeInt(int i) {
        if (!writeable) throw new IllegalStateException("Attempted to write to non writeable byte buf!");
        writeByte((byte) (i >>> 24));
        writeByte((byte) ((i & 0xFF0000) >>> 16));
        writeByte((byte) ((i & 0xFF00) >>> 8));
        writeByte((byte) (i & 0xFF));
    }


    public byte readByte() {
        if (readableBytes() < 1) throw new IndexOutOfBoundsException("Tried to read byte out of bounds of byteBuf!");
        return buf[readerIndex++];
    }

    public void readBytes(byte[] dst) {
        if (readableBytes() < 1) throw new IndexOutOfBoundsException();
        System.arraycopy(buf, readerIndex, dst, 0, readableBytes());
        readerIndex++;
    }

    public int readInt() {
        int i;
        i = (readByte() & 0xFF) << 24;
        i |= (readByte() & 0xFF) << 16;
        i |= (readByte() & 0xFF) << 8;
        i |= (readByte() & 0xFF);
        return i;
    }

    // Slow but idc
    public void writeBytes(byte[] src) {
        for (byte b : src) {
            writeByte(b);
        }
    }

    public void writeString(String s) {
        if (!this.writeable) throw new IllegalStateException("Tried to write non-writeable ByteBuf!");
        writeInt(s.length());
        for (byte b : s.getBytes(StandardCharsets.UTF_8)) {

            writeByte(b);
        }
    }

    public String readString() {
        int size = readInt();
        byte[] buffer = new byte[size];
        for (int i = 0; i < size; i++) {
            buffer[i] = readByte();
        }
        return new String(buffer, StandardCharsets.UTF_8);
    }

    // Make some tests

    // read int
    // write bytes
    // read bytes

    // write string
    // read string

    public String toString() {
        return Arrays.toString(buf);
    }
}
