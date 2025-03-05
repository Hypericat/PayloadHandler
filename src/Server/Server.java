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
    private static ClientNetworkHandler clientNetworkHandler;  // Create instance for handling communication

    public static void run() {
        System.out.println("Running server!");
        networkHandler = new ServerNetworkHandler();

        while (true) {
            if (!networkHandler.readConnections(NetworkUtil.port)) {
                System.out.println("Failed to connect!");
                continue;
            }

            System.out.println("Client connected!");

            // Handle packets for connected client
            clientNetworkHandler = new ClientNetworkHandler();  // Create new instance of ClientNetworkHandler
            handleIncomingPackets();

            networkHandler.close(); // Close connection after handling
            return;
        }
    }


    public static IPacket receivePacket() {
        try {
            if (clientNetworkHandler.getInputStream() == null) return null;  // Ensure inputStream is accessible

            int length = clientNetworkHandler.getInputStream().readInt();  // Read packet length
            byte[] rawData = new byte[length];
            clientNetworkHandler.getInputStream().readFully(rawData);  // Read packet data into rawData

            ByteBuf buf = new ByteBuf(rawData);
            byte packetID = buf.readByte();  // Read packet ID

            // Use PacketRegistry to create correct packet based on packetID
            IPacket packet = PacketRegistry.createPacket(packetID);
            if (packet != null) {
                packet.decode(buf);  // Decode packet
                return packet;
            }

            System.err.println("Unknown packet ID received: " + packetID);
        } catch (IOException e) {
            System.err.println("Error receiving packet: " + e.getMessage());
        }
        return null;
    }


    public static boolean sendPacket(IPacket packet) {
        try {
            if (clientNetworkHandler.getOutputStream() == null) return false;  // Ensure outputStream is accessible

            ByteBuf buf = new ByteBuf();
            buf.writeByte(packet.getPacketID());  // Write packet ID first
            int length = packet.encode(buf);  // Encode packet, get its length

            clientNetworkHandler.getOutputStream().writeInt(length);  // Send packet length first
            clientNetworkHandler.getOutputStream().write(buf.getRawBytes(), 0, length);  // Send encoded packet
            clientNetworkHandler.getOutputStream().flush();

            return true;
        } catch (IOException e) {
            System.err.println("Error sending packet: " + e.getMessage());
        }
        return false;
    }

    private static void handleIncomingPackets() {
        while (true) {
            IPacket packet = receivePacket();  // Attempt to receive packet

            if (packet != null) {
                System.out.println("Received packet with ID: " + packet.getPacketID());

                // Check if packet is valid
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



    private static void processPacket(IPacket packet) {
        byte packetID = packet.getPacketID();

        switch (packetID) {
            case 0x00 ->  // Example packet ID for Handshake
                    System.out.println("Processing HandshakePacket");
            default -> System.out.println("Unknown packet type: " + packetID);
        }
    }
}