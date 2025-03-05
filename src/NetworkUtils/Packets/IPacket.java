package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;

public interface IPacket {
    void decode(ByteBuf buf);

    // Encode return the length of the packet
    int encode(ByteBuf buf);

    byte getPacketID();

    String toString();
}
