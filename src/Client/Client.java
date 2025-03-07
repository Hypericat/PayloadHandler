package Client;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;
import NetworkUtils.Packet;
import NetworkUtils.PacketHandler;
import NetworkUtils.Packets.HandshakePacket;
import NetworkUtils.Packets.PrintPacket;

import java.io.File;
import java.util.List;

public class Client {

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

        networkHandler.getConnection().sendPacket(new HandshakePacket(new File(System.getProperty("user.home")).getName()));

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
            packet.execute(packetHandler);
        });

        //networkHandler.getConnection().receive();
        //networkHandler.getConnection().send();
    }

}
