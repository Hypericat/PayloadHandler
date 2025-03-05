import Server.Server;
import Client.Client;
import AdminClient.AdminClient;
import NetworkUtils.PacketRegistry;
import NetworkUtils.Packets.HandshakePacket;

public class Main {
    public static void main(String[] args) {
        registerPackets();

        // Check if argument "admin" is passed to run Admin Client
        if (args.length > 0 && args[0].equalsIgnoreCase("admin")) {
            AdminClient.run();
        } else if (args.length == 0) {
            Client.run();
        } else {
            Server.run();
        }
    }

    private static void registerPackets() {
        // Register the HandshakePacket with its packet ID
        PacketRegistry.registerPacket((byte) 0x00, HandshakePacket.class);
        System.out.println("Registered packets.");
    }
}
