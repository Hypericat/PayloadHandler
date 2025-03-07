package AdminClient;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.AdminIDPacket;
import NetworkUtils.Packets.PrintPacket;
import NetworkUtils.Packets.AdminCommandPacket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
                    Thread.sleep(msRetryFinalConnect);
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            break;
        }

        System.out.println("Established a connection!");
        packetHandler = new PacketHandler(networkHandler.getConnection());

        // Send Admin ID to the server for verification
        String adminID = "Winston smells";
        networkHandler.getConnection().sendPacket(new AdminIDPacket(adminID));

        // Start CLI loop for sending commands
        startAdminCLI();
    }

    public static void startAdminCLI() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.println("Enter command to send to server:");
                String command = reader.readLine().trim();
                if (command.isEmpty()) continue;

                // Send the command to the server
                AdminCommandPacket adminCommandPacket = new AdminCommandPacket(command);
                networkHandler.getConnection().sendPacket(adminCommandPacket);
                System.out.println("Command sent to server: " + command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loop() {
        List<Packet> packets = networkHandler.getConnection().parseReceivedPackets();

        packets.forEach(packet -> {
            System.out.println("Received packet: " + packet.toString());

            if (packet instanceof PrintPacket) {
                PrintPacket printPacket = (PrintPacket) packet;
                System.out.println("Server Response: " + printPacket.getMessage());
            }

            packet.execute(packetHandler);
        });
    }
}
