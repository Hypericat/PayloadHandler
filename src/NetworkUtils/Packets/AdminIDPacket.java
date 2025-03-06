package NetworkUtils.Packets;

import NetworkUtils.ByteBuf;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;

public class AdminIDPacket extends Packet {
    private String adminID;

    public AdminIDPacket() {

    }


    public AdminIDPacket(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminID() {
        return adminID;
    }

    @Override
    public void decode(ByteBuf buf) {
        // Decode the AdminID from the buffer
        this.adminID = buf.readString();
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeString(adminID);
    }

    @Override
    public byte getPacketID() {
        return 0x01;
    }

    @Override
    public String toString() {
        return "Admin ID Packet, AdminID: " + adminID;
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onAdminID(this);
    }
}
