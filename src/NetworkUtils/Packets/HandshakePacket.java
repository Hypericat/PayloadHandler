package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.IPacket;

public class HandshakePacket implements IPacket {

    public HandshakePacket() {

    }

    @Override
    public void decode(ByteBuf buf) {

    }

    @Override
    public void encode(ByteBuf buf) {

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
