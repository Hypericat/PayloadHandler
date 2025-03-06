package AdminClient;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.AdminIDPacket;
import NetworkUtils.Packets.PrintPacket;

import java.util.List;

public class AdminClient {

    public static final int secondsRetryConnect = 5;
    public static final int msRetryFinalConnect = secondsRetryConnect * 1000;

    private static ClientNetworkHandler networkHandler;
    private static PacketHandler packetHandler;

    public static void run() {
        networkHandler = new ClientNetworkHandler();

        // Init connection
        while (true) {
            if (!networkHandler.connect(NetworkUtil.port, NetworkUtil.serverDDNS)) {
                System.out.println("Connection failed. Attempting to reconnect...");
                try {
                    Thread.sleep(msRetryFinalConnect); // Suspend execution
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            break;
        }
        System.out.println("Established a connection!");
        packetHandler = new PacketHandler(networkHandler.getConnection());


        String adminID = "fuck u nigga";
        networkHandler.getConnection().sendPacket(new AdminIDPacket(adminID));

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
        List<Packet> packets = networkHandler.getConnection().parseReceivedPackets();
        packets.forEach(packet -> {
            System.out.println("Received packet : " + packet.toString());

            if (packet instanceof PrintPacket printPacket) {
                String message = printPacket.getMessage();
                System.out.println("Server Response: " + message);


                if (message.contains("Invalid Admin ID")) {
                    System.out.println("Admin authentication failed. Closing client...");
                    System.exit(1);
                }
            }

            packet.execute(packetHandler);
        });
    }
}