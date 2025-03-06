package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class AdminPacket extends Packet {

    @Override
    public void decode(ByteBuf buf) {
        // If you want to decode anything in the future (e.g., admin password), do it here
    }

    @Override
    public void encode(ByteBuf buf) {

    }

    @Override
    public byte getPacketID() {
        return 0x01;
    }

    @Override
    public String toString() {
        return "Admin Packet";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onAdmin(this);
    }
}
