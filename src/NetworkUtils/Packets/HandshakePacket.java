package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;

import java.nio.ByteBuffer;

public class HandshakePacket implements IPacket {

    public HandshakePacket() {

    }

    @Override
    public void decode(ByteBuf buf) {

    }

    @Override
    public int encode(ByteBuf buf) {
        //buf.writeVarInt(1);
        //buf.writeByte(this.getPacketID());
        return 1;
    }

    @Override
    public byte getPacketID() {
        return 0x00;
    }

    @Override
    public String toString() {
        return "Handshake";
    }


}
