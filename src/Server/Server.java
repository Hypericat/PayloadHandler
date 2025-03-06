package Server;

import NetworkUtils.NetworkUtil;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.SpeakPacket;
import NetworkUtils.Packets.WebsitePacket;
import Server.Networking.ServerNetworkHandler;
import NetworkUtils.Packet;

import java.util.List;

public class Server {
    private static ServerNetworkHandler networkHandler;
    private static PacketHandler packetHandler;

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
        packetHandler = new PacketHandler(networkHandler.getConnection(0));

        networkHandler.getConnection(0).sendPacket(new SpeakPacket("Shut yo bitch ass up nigga, this shit pissing me off"));
        //for (int i = 0; i < 1000; i++)
        //    networkHandler.getConnection(0).sendPacket(new WebsitePacket("https://www.humaneworld.org/sites/default/files/styles/responsive_3_4_500w/public/2023-05/pet-rat-606079.jpg.webp?itok=NjEL3kef"));

        while (true) {

            try {
                loop();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loop() {
        List<Packet> packets = networkHandler.getConnection(0).parseReceivedPackets();
        packets.forEach(packet -> {
            System.out.println("Received packet : " + packet.toString());
            packet.execute(packetHandler);
        });
    }
}
