package NetworkUtils;

import NetworkUtils.Packets.AdminPacket;
import NetworkUtils.Packets.HandshakePacket;
import NetworkUtils.Packets.PrintPacket;

public class PacketHandler {
    private SocketConnection connection;

    public PacketHandler(SocketConnection connection) {
        this.connection = connection;
    }

    public void onHandshake(HandshakePacket packet) {

    }

    public void onAdmin(AdminPacket packet) {

    }

    public void onPrint(PrintPacket packet) {
         System.out.println("[Message] : " + packet.getMessage());
    }
}
