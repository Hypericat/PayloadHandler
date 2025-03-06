package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class SpeakPacket extends Packet {

    private String message;

    public SpeakPacket() {

    }

    public SpeakPacket(String message) {
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
        return 0x04;
    }

    @Override
    public String toString() {
        return "SpeakPacket";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onSpeak(this);
    }

    public String getMessage() {
        return this.message;
    }


}
