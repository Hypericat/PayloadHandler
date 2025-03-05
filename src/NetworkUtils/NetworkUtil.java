package NetworkUtils;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.Packets.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NetworkUtil {

    public static final short port = 420;
    public static final String serverDDNS = "winstonchurchili.ddns.net";

    // Send a packet using the ClientNetworkHandler
    public static boolean sendPacket(IPacket packet, ClientNetworkHandler networkHandler) {
        try {
            if (networkHandler == null || networkHandler.getOutputStream() == null) return false;

            // Create ByteBuf and write packet ID and encoded packet data
            ByteBuf buf = new ByteBuf();
            buf.writeByte(packet.getPacketID());
            packet.encode(buf);
            buf.writerIndex(0);
            buf.writeInt(buf.getWrittenByteCount());

            // Send length and encoded packet data to server
            DataOutputStream out = networkHandler.getOutputStream();
            out.write(buf.getRawBytes());
            out.flush();

            return true;
        } catch (IOException e) {
            System.err.println("Error sending packet: " + e.getMessage());
        }
        return false;
    }

    // Receive a packet from the server
    public static IPacket receivePacket(ClientNetworkHandler networkHandler) {
        try {
            if (networkHandler == null || networkHandler.getInputStream() == null) return null;

            DataInputStream in = networkHandler.getInputStream();

            int length = in.readInt();  // Read packet length
            byte[] rawData = new byte[length];
            in.readFully(rawData);  // Read packet data into rawData

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
