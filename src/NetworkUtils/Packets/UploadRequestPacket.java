package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class UploadRequestPacket extends Packet {

    private String srcFilePath;  // Source file path on the client
    private String dstFilePath;  // Destination file path on the server

    public UploadRequestPacket() {

    }

    public UploadRequestPacket(String fileName, String srcFilePath, String dstFilePath) {
        this.srcFilePath = srcFilePath;
        this.dstFilePath = dstFilePath;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.srcFilePath = buf.readString();  // Decode the source file path from the buffer
        this.dstFilePath = buf.readString();  // Decode the destination file path from the buffer
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(srcFilePath);  // Write the source file path to the buffer
        buf.writeString(dstFilePath);  // Write the destination file path to the buffer
    }

    @Override
    public byte getPacketID() {
        return 0x08;  // Unique packet ID for UploadRequestPacket
    }

    @Override
    public String toString() {
        return "UploadRequestPacket{" +
                ", srcFilePath='" + srcFilePath + '\'' +
                ", dstFilePath='" + dstFilePath + '\'' +
                '}';
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onUploadRequest(this);  // Execute the handler logic for this packet
    }

    public String getSrcFilePath() {
        return this.srcFilePath;
    }

    public String getDstFilePath() {
        return this.dstFilePath;
    }
}
