package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class UploadRequestPacket extends Packet {

    private String fileSrc;  // Only the source path is needed
    private int id;

    public UploadRequestPacket() {
    }

    public UploadRequestPacket(int id, String fileSrc) {
        this.id = id;
        this.fileSrc = fileSrc;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.id = buf.readInt();
        this.fileSrc = buf.readString();  // Decode the source path
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(id);               // Write file ID
        buf.writeString(fileSrc);       // Write source path
    }

    @Override
    public byte getPacketID() {
        return 0x06;  // Unique ID for UploadRequestPacket
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

    public int getId() {
        return this.id;
    }
}
