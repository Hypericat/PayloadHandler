package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class ChangeDirectoryPacket extends Packet {

    private String path;

    public ChangeDirectoryPacket() {

    }

    public ChangeDirectoryPacket(String path) {
        this.path = path;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.path = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(path);
    }

    @Override
    public byte getPacketID() {
        return 0x09;
    }

    @Override
    public String toString() {
        return "ChangeDirectoryPacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onChangeDirectory(this);
    }

    public String getPath() {
        return this.path;
    }
}
