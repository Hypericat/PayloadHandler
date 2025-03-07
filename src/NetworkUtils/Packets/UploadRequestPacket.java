package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class UploadRequestPacket extends Packet {

    private String fileSrc;
    private String fileDst;
    private int id;

    public UploadRequestPacket() {
    }

    public UploadRequestPacket(int id, String fileSrc, String fileDst) {
        this.id = id;
        this.fileSrc = fileSrc;
        this.fileDst = fileDst;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.id = buf.readInt();
        this.fileSrc = buf.readString();  // Decode the source path
        this.fileDst = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(id);               // Write file ID
        buf.writeString(fileSrc);       // Write source path
        buf.writeString(fileDst);
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

    // Getters
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
