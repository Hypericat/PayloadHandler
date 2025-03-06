package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class FileChunkPacket extends Packet {

    private byte[] data;  // The actual chunk of data
    private long offset;  // The offset of this chunk in the file (useful for uploads)
    private int operationId;  // Unique ID for the operation

    // Default constructor
    public FileChunkPacket() {
    }

    // Constructor to initialize with chunk data, offset, and operationId
    public FileChunkPacket(byte[] data, long offset, int operationId) {
        this.data = data;
        this.offset = offset;
        this.operationId = operationId;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.operationId = buf.readInt();  // Read the operation ID
        this.offset = buf.readInt();

        int dataLength = buf.readInt();  // Read the length of the byte array
        this.data = new byte[dataLength];

        for (int i = 0; i < dataLength; i++) {
            this.data[i] = buf.readByte();  // Read the bytes one by one
        }
    }

    @Override
    public void encode(ByteBuf buf) {

        buf.writeInt(operationId);
        buf.writeInt((int) offset);
        buf.writeInt(data.length);
        for (byte b : data) {
            buf.writeByte(b);
        }
    }

    @Override
    public byte getPacketID() {
        return 0x06;
    }

    @Override
    public String toString() {
        return "FileChunkPacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onFileChunk(this);
    }

    public byte[] getData() {
        return this.data;
    }

    public long getOffset() {
        return this.offset;
    }

    public int getOperationId() {
        return this.operationId;
    }
}
