package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class AdminCommandPacket extends Packet {
    private String command;

    public AdminCommandPacket() {
    }

    public AdminCommandPacket(String command) {
        this.command = command;
    }

    @Override
    public void decode(ByteBuf buf) {
        this.command = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(command);
    }

    @Override
    public byte getPacketID() {
        return 0x0C;  // Unique ID for AdminCommandPacket
    }

    @Override
    public String toString() {
        return "AdminCommandPacket: " + command;
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onAdminCommand(this);
    }

    public String getCommand() {
        return command;
    }
}
