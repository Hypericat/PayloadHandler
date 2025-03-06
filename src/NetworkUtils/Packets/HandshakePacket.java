package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class HandshakePacket extends Packet {

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
        return "HandshakePacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onHandshake(this);
    }


}
