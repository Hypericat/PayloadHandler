package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class UploadRequestPacket extends Packet {

    private String fileName;
    private int fileSize;

    public UploadRequestPacket() {

    }

    public UploadRequestPacket(String fileName, int fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.fileName = buf.readString();
        this.fileSize = buf.readInt();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(fileName);
        buf.writeInt(fileSize);
    }

    @Override
    public byte getPacketID() {
        return 0x08;
    }

    @Override
    public String toString() {
        return "UploadRequestPacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onUploadRequest(this);
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getFileSize() {
        return this.fileSize;
    }
}
