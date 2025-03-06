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
        this.adminID = buf.readString(); // Assuming there's a method like readString in ByteBuf
    }

    @Override
    public void encode(ByteBuf buf) {
        // Encode the AdminID into the buffer
        buf.writeString(adminID); // Assuming there's a method like writeString in ByteBuf
    }

    @Override
    public byte getPacketID() {
        return 0x01; // Unique ID for the AdminID packet
    }

    @Override
    public String toString() {
        return "Admin ID Packet, AdminID: " + adminID;
    }

    @Override
    public void execute(PacketHandler handler) {
        handler.onAdminID(this); // Calls the server-side handler to verify AdminID
    }
}
