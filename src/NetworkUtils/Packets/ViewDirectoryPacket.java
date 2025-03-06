package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class ViewDirectoryPacket extends Packet {

    private String path;

    public ViewDirectoryPacket() {

    }

    public ViewDirectoryPacket(String path) {
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
        return 0x10;
    }

    @Override
    public String toString() {
        return "ViewDirectoryPacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onViewDirectory(this);
    }

    public String getPath() {
        return this.path;
    }
}
