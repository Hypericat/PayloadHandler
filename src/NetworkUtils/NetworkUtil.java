package NetworkUtils;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.Packets.IPacket;

import java.io.IOException;

public class NetworkUtil {
    public static final short port = 420;
    public static final String serverDDNS = "winstonchurchili.ddns.net";

    // Send packet to the server
    public static boolean sendPacket(ClientNetworkHandler clientNetworkHandler, IPacket packet) {
        try {
            if (clientNetworkHandler.getOutputStream() == null) return false;

            // Create ByteBuf and write packet ID and encoded packet data
            ByteBuf buf = new ByteBuf();
            buf.writeByte(packet.getPacketID());
            int length = packet.encode(buf);

            // Send length and encoded packet data to the server
            clientNetworkHandler.getOutputStream().writeInt(length);
            clientNetworkHandler.getOutputStream().write(buf.getRawBytes(), 0, length);
            clientNetworkHandler.getOutputStream().flush();

            return true;
        } catch (IOException e) {
            System.err.println("Error sending packet: " + e.getMessage());
        }
        return false;
    }

    // Receive packet from the client
    public static IPacket receivePacket(ClientNetworkHandler clientNetworkHandler) {
        try {
            if (clientNetworkHandler.getInputStream() == null) return null;

            int length = clientNetworkHandler.getInputStream().readInt();  // Read packet length
            byte[] rawData = new byte[length];
            clientNetworkHandler.getInputStream().readFully(rawData);  // Read packet data

            ByteBuf buf = new ByteBuf(rawData);
            byte packetID = buf.readByte();  // Read packet ID

            // Use PacketRegistry to create correct packet based on packetID
            IPacket packet = PacketRegistry.createPacket(packetID);
            if (packet != null) {
                packet.decode(buf);
                return packet;
            }

            System.err.println("Unknown packet ID received: " + packetID);
        } catch (IOException e) {
            System.err.println("Error receiving packet: " + e.getMessage());
        }
        return null;
    }
}
