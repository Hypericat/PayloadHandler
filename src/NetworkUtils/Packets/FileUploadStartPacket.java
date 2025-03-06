package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class FileUploadStartPacket extends Packet {

    private String fileName;
    private String filePath;
    private int id;

    public FileUploadStartPacket() {

    }

    public FileUploadStartPacket(String fileName, int id) {
        this.fileName = fileName;
        this.id = id;
    }

    public FileUploadStartPacket(String fileName, String filePath, int id) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.id = id;

    }

    @Override
    public void decode(ByteBuf buf) {
        this.id = buf.readInt();
        this.fileName = buf.readString();
        this.filePath = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(id);
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
    public int getId() {
        return this.id;
    }


}
