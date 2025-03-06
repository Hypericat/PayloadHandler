package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class FileCompletePacket extends Packet {

    private int id;

    public FileCompletePacket() {

    }

    public FileCompletePacket(int id) {
        this.id = id;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.id = buf.readInt();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(id);
    }

    @Override
    public byte getPacketID() {
        return 0x07;
    }

    @Override
    public String toString() {
        return "FileCompletePacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onFileComplete(this);
    }

    public int getId() {
        return this.id;
    }
}
