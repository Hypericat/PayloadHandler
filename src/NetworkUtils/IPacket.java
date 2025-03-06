package NetworkUtils;

public interface IPacket {
    void decode(ByteBuf buf);

    // Encode return the length of the packet
    void encode(ByteBuf buf);

    byte getPacketID();

    String toString();
}
