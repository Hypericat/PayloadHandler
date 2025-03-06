package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class FileChunkPacket extends Packet {

    private byte[] data;  // The actual chunk of data
    private int id;  // Unique ID for the operation

    // Default constructor
    public FileChunkPacket() {
    }

    // Constructor to initialize with chunk data, offset, and operationId
    public FileChunkPacket(byte[] data, int id) {
        this.data = data;
        this.id = id;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.id = buf.readInt();  // Read the operation ID
        int dataLength = buf.readInt();  // Read the length of the byte array
        this.data = new byte[dataLength];

        for (int i = 0; i < dataLength; i++) {
            this.data[i] = buf.readByte();  // Read the bytes one by one
        }
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(id);
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


    public int getId() {
        return this.id;
    }
}
