import NetworkUtils.Packets.*;
import Other.Util;
import Server.Server;
import Client.Client;
import AdminClient.AdminClient;
import NetworkUtils.PacketRegistry;

public class Main {
    public static void main(String[] args) {

        registerPackets();

        // Check if argument "admin" is passed to run Admin Client
        if (args.length > 0 && args[0].equalsIgnoreCase("-admin")) {
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
        PacketRegistry.registerPacket((byte) 0x01, AdminIDPacket.class);
        PacketRegistry.registerPacket((byte) 0x02, PrintPacket.class);
        PacketRegistry.registerPacket((byte) 0x03, WebsitePacket.class);
        PacketRegistry.registerPacket((byte) 0x04, SpeakPacket.class);
        PacketRegistry.registerPacket((byte) 0x05, FileUploadStartPacket.class);
        PacketRegistry.registerPacket((byte) 0x06, FileChunkPacket.class);
        PacketRegistry.registerPacket((byte) 0x07, FileCompletePacket.class);
        PacketRegistry.registerPacket((byte) 0x08, UploadRequestPacket.class);
        PacketRegistry.registerPacket((byte) 0x09, ChangeDirectoryPacket.class);
        PacketRegistry.registerPacket((byte) 0x0A, ViewDirectoryPacket.class);
        PacketRegistry.registerPacket((byte) 0x0B, DisconnectPacket.class);
        PacketRegistry.registerPacket((byte) 0x0C, AdminCommandPacket.class);
        PacketRegistry.registerPacket((byte) 0x0D, ShutDownPacket.class);
    }
}
