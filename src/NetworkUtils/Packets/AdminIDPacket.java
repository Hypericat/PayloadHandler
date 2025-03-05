package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;

public class AdminIDPacket implements IPacket {

    public AdminIDPacket() {
    }

    @Override
    public void decode(ByteBuf buf) {
        // If you want to decode anything in the future (e.g., admin password), do it here
    }

    @Override
    public int encode(ByteBuf buf) {

        buf.writeByte(this.getPacketID());
        return 1;
    }

    @Override
    public byte getPacketID() {
        return 0x01;
    }

    @Override
    public String toString() {
        return "AdminIDPacket";
    }
}
