package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class UploadRequestPacket extends Packet {

    private String fileName;  // The file name being uploaded
    private String srcFilePath;  // Source file path on the client
    private String destFilePath;  // Destination file path on the server

    public UploadRequestPacket() {

    }

    public UploadRequestPacket(String fileName, String srcFilePath, String destFilePath) {
        this.fileName = fileName;
        this.srcFilePath = srcFilePath;
        this.destFilePath = destFilePath;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.fileName = buf.readString();  // Decode the file name from the buffer
        this.srcFilePath = buf.readString();  // Decode the source file path from the buffer
        this.destFilePath = buf.readString();  // Decode the destination file path from the buffer
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(fileName);  // Write the file name to the buffer
        buf.writeString(srcFilePath);  // Write the source file path to the buffer
        buf.writeString(destFilePath);  // Write the destination file path to the buffer
    }

    @Override
    public byte getPacketID() {
        return 0x08;  // Unique packet ID for UploadRequestPacket
    }

    @Override
    public String toString() {
        return "UploadRequestPacket{" +
                "fileName='" + fileName + '\'' +
                ", srcFilePath='" + srcFilePath + '\'' +
                ", destFilePath='" + destFilePath + '\'' +
                '}';
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onUploadRequest(this);  // Execute the handler logic for this packet
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getSrcFilePath() {
        return this.srcFilePath;
    }

    public String getDestFilePath() {
        return this.destFilePath;
    }
}
