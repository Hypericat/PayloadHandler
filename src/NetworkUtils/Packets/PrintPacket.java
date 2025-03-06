package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class PrintPacket extends Packet {

    private String message;

    public PrintPacket() {

    }

    public PrintPacket(String message) {
        this.message = message;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.message = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(message);
    }

    @Override
    public byte getPacketID() {
        return 0x02;
    }

    @Override
    public String toString() {
        return "PrintPacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onPrint(this);
    }

    public String getMessage() {
        return this.message;
    }


}
