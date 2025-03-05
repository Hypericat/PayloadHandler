package Client.Networking;

import NetworkUtils.PacketRegistry;
import NetworkUtils.Packets.IPacket;
import NetworkUtils.ByteBuf;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientNetworkHandler {
    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static final int CONNECT_TIMEOUT = 5000;

    public ClientNetworkHandler() {
    }

    // Connect to server with timeout
    public boolean connect(short port, String serverHostname) {
        try {
            InetAddress serverAddress = InetAddress.getByName(serverHostname);
            System.out.println("Resolved IP: " + serverAddress.getHostAddress());

            // Create new socket and connect with timeout
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, port), CONNECT_TIMEOUT);

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connected to the server.");
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    // Method to send packet to server
    public boolean sendPacket(IPacket packet) {
        try {
            if (outputStream == null) return false;

            // Create ByteBuf and write packet ID and encoded packet data
            ByteBuf buf = new ByteBuf();
            buf.writeByte(packet.getPacketID());
            int length = packet.encode(buf);

            // Send length and encoded packet data to server
            outputStream.writeInt(length);
            outputStream.write(buf.getRawBytes(), 0, length);
            outputStream.flush();

            return true;
        } catch (IOException e) {
            System.err.println("Error sending packet: " + e.getMessage());
        }
        return false;
    }

    // Method to receive packet from server
    public IPacket receivePacket() {
        try {
            if (inputStream == null) return null;

            int length = inputStream.readInt();  // Read packet length
            byte[] rawData = new byte[length];
            inputStream.readFully(rawData);  // Read packet data

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

    // Close connection and streams
    public void close() {
        try {
            if (inputStream != null) { inputStream.close(); }
            if (outputStream != null) { outputStream.close(); }
            if (socket != null && !socket.isClosed()) { socket.close(); }
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
