package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class FileUploadStartPacket extends Packet {

    private String fileSrc;
    private String fileDst;
    private int id;

    public FileUploadStartPacket() {

    }

    public FileUploadStartPacket(String fileSrc, int id) {
        this.fileSrc = fileSrc;
        this.id = id;
    }

    public FileUploadStartPacket(String fileSrc, String fileDst, int id) {
        this.fileSrc = fileSrc;
        this.fileDst = fileDst;
        this.id = id;

    }

    @Override
    public void decode(ByteBuf buf) {
        this.id = buf.readInt();
        this.fileSrc = buf.readString();
        this.fileDst = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeString(fileSrc);
        buf.writeString(fileDst);
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

    public String getFileSrc() {
        return this.fileSrc;
    }
    public String getFileDst() {
        return this.fileDst;
    }
    public int getId() {
        return this.id;
    }


}
