import Server.Server;
import Client.Client;
import NetworkUtils.PacketRegistry;
import NetworkUtils.Packets.HandshakePacket;

public class Main {
    public static void main(String[] args) {
        // Register packets before running the client or server
        registerPackets();

        if (args.length == 0) {
            Client.run();
            return;
        }

        Server.run();
    }

    private static void registerPackets() {
        PacketRegistry.registerPacket((byte) 0x00, HandshakePacket.class);
        System.out.println("Registered packets.");
    }
}
