package NetworkUtils;

public abstract class Packet {
    public Packet() {

    }

    public abstract void decode(ByteBuf buf);
    public abstract void encode(ByteBuf buf);

    public abstract byte getPacketID();
    public abstract String toString();

    public abstract void execute(PacketHandler handler);



}
