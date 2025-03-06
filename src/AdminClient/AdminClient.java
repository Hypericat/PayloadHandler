package AdminClient;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.PrintPacket;
import NetworkUtils.Packets.AdminIDPacket; // Import the new packet

import java.util.List;

public class AdminClient {

    public static final int secondsRetryConnect = 0;
    public static final int msRetryConnect = 0;
    public static final int msRetryFinalConnect = secondsRetryConnect * 1000 + msRetryConnect;

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

        // Send AdminID packet to server for verification
        String adminID = "Winston smells";

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
            packet.execute(packetHandler);
        });

        //networkHandler.getConnection().receive();
        //networkHandler.getConnection().send();
    }
}
