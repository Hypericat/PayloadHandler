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


        networkHandler.close(); // Close connection after handling
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