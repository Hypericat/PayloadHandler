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
    private static ClientNetworkHandler clientNetworkHandler;  // Instance for client communication
    private static final byte ADMIN_PACKET_ID = 0x01;  // Admin Client packet identifier

    public static void run() {
        System.out.println("Running server!");
        networkHandler = new ServerNetworkHandler();

        while (true) {
            if (!networkHandler.readConnections(NetworkUtil.port)) {
                System.out.println("Failed to connect!");
                continue;
            }
            break;
        }
        System.out.println("Client connected!");

        // Handle packets for connected client
        handleIncomingPackets();
    }

    private static void handleIncomingPackets() {
        while (true) {
            IPacket packet = NetworkUtil.receivePacket(clientNetworkHandler);  // Attempt to receive packet

            if (packet != null) {
                System.out.println("Received packet with ID: " + packet.getPacketID());

                // Check if the packet is from an Admin Client
                if (packet.getPacketID() == ADMIN_PACKET_ID) {
                    // If the packet ID matches the Admin ID, print "Admin Client connected"
                    System.out.println("Admin client connected!");
                } else {
                    System.out.println("Client connected!");
                }

                // Check if the packet is valid and process it
                if (PacketRegistry.createPacket(packet.getPacketID()) != null) {
                    processPacket(packet);
                } else {
                    System.out.println("Received an invalid packet: " + packet.getPacketID());
                }
            } else {
                System.out.println("No packet received. Waiting for next connection...");
                break;
            }
        }
    }

    // Process the received packet based on its ID
    private static void processPacket(IPacket packet) {
        byte packetID = packet.getPacketID();

        switch (packetID) {
            case 0x00:  // Example packet ID for Handshake
                System.out.println("Processing HandshakePacket");
                break;

            // Add cases for handling other packets here if needed
            default:
                System.out.println("Unknown packet type: " + packetID);
                break;
        }
    }
}
