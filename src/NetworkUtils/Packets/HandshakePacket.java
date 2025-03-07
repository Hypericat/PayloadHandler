package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class HandshakePacket extends Packet {

    String path;

    public HandshakePacket() {

    }

    public HandshakePacket(String userFolderPath) {
        this.path = userFolderPath;
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
        return 0x00;
    }

    @Override
    public String toString() {
        return "HandshakePacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onHandshake(this);
    }

    public String getPath() {
        return path;
    }
}
