package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class FileUploadStartPacket extends Packet {

    private String fileName;
    private String filePath;

    public FileUploadStartPacket() {

    }

    public FileUploadStartPacket(String fileName) {
        this.fileName = fileName;
    }

    public FileUploadStartPacket(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.fileName = buf.readString();
        this.filePath = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(fileName);
        buf.writeString(filePath);
    }

    @Override
    public byte getPacketID() {
        return 0x05;
    }

    @Override
    public String toString() {
        return "UploadStartPacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onUploadStart(this);
    }

    public String getFileName() {
        return this.fileName;
    }
    public String getFilePath() {
        return this.filePath;
    }


}
