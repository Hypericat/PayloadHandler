package Server;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.ByteBuf;
import NetworkUtils.NetworkUtil;
import Server.Networking.ServerNetworkHandler;
import NetworkUtils.Packets.IPacket;
import NetworkUtils.PacketRegistry;

import java.io.IOException;

public class Server {
    private static ServerNetworkHandler networkHandler;
    private static ClientNetworkHandler clientNetworkHandler;  // Create an instance for handling client communication

    public static void run() {
        System.out.println("Running server!");
        networkHandler = new ServerNetworkHandler();

        while (true) {
            if (!networkHandler.readConnections(NetworkUtil.port)) {
                System.out.println("Failed to connect!");
                continue;
            }

            System.out.println("Client connected!");

            // Handle incoming packets for the connected client
            clientNetworkHandler = new ClientNetworkHandler();  // Create a new instance of ClientNetworkHandler
            handleIncomingPackets();  // Start handling incoming packets for the client

            networkHandler.close(); // Close the connection after handling
            return;
        }
    }

    /**
     * Method to receive a packet from the client.
     * Now this is using the clientNetworkHandler instance.
     */
    public static IPacket receivePacket() {
        try {
            if (clientNetworkHandler.getInputStream() == null) return null;  // Ensure inputStream is accessible

            int length = clientNetworkHandler.getInputStream().readInt();  // Read the packet length
            byte[] rawData = new byte[length];
            clientNetworkHandler.getInputStream().readFully(rawData);  // Read the packet data into rawData

            ByteBuf buf = new ByteBuf(rawData);
            byte packetID = buf.readByte();  // Read the packet ID

            // Use the PacketRegistry to create the correct packet based on packetID
            IPacket packet = PacketRegistry.createPacket(packetID);
            if (packet != null) {
                packet.decode(buf);  // Decode the packet
                return packet;
            }

            System.err.println("Unknown packet ID received: " + packetID);
        } catch (IOException e) {
            System.err.println("Error receiving packet: " + e.getMessage());
        }
        return null;
    }

    /**
     * Method to send a packet to the client.
     * Using the clientNetworkHandler instance to access outputStream.
     */
    public static boolean sendPacket(IPacket packet) {
        try {
            if (clientNetworkHandler.getOutputStream() == null) return false;  // Ensure outputStream is accessible

            ByteBuf buf = new ByteBuf();
            buf.writeByte(packet.getPacketID());  // Write packet ID first
            int length = packet.encode(buf);  // Encode packet and get its length

            clientNetworkHandler.getOutputStream().writeInt(length);  // Send the packet length first
            clientNetworkHandler.getOutputStream().write(buf.getRawBytes(), 0, length);  // Send the encoded packet
            clientNetworkHandler.getOutputStream().flush();

            return true;
        } catch (IOException e) {
            System.err.println("Error sending packet: " + e.getMessage());
        }
        return false;
    }

    /**
     * Handle incoming packets in a loop for the connected client.
     * Uses the receivePacket() method defined above.
     */
    private static void handleIncomingPackets() {
        while (true) {
            IPacket packet = receivePacket();  // Receive packet from client

            if (packet != null) {
                System.out.println("Received packet: " + packet);

                // Process the packet based on its type
                processPacket(packet);
            } else {
                System.out.println("Received unknown packet type.");
            }
        }
    }

    /**
     * Process the received packet based on its ID.
     */
    private static void processPacket(IPacket packet) {
        byte packetID = packet.getPacketID();

        switch (packetID) {
            case 0x00:  // Example packet ID for Handshake
                System.out.println("Processing HandshakePacket");
                break;

            default:
                System.out.println("Unknown packet type: " + packetID);
                break;
        }
    }
}
