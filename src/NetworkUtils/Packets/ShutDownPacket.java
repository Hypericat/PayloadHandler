package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class ShutDownPacket extends Packet {
    private int msWaitTime;

    public ShutDownPacket() {

    }

    public ShutDownPacket(int msWait) {
        this.msWaitTime = msWait;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.msWaitTime = buf.readInt();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(msWaitTime);
    }

    @Override
    public byte getPacketID() {
        return 0x0D;
    }

    @Override
    public String toString() {
        return "Shutdown Packet";
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onShutDown(this);
    }

    public int getMsWaitTime() {
        return this.msWaitTime;
    }
}
