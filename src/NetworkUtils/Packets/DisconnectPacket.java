package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class DisconnectPacket extends Packet {

    public DisconnectPacket() {

    }

    @Override
    public void decode(ByteBuf buf) {

    }

    @Override
    public void encode(ByteBuf buf) {

    }

    @Override
    public byte getPacketID() {
        return 0x11;
    }

    @Override
    public String toString() {
        return "DisconnectPacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onDisconnect(this);
    }


}
