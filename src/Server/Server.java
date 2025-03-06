package Server;

import NetworkUtils.NetworkUtil;
import Server.Networking.ServerNetworkHandler;
import NetworkUtils.IPacket;
import NetworkUtils.PacketRegistry;

import java.util.List;

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
        List<IPacket> packets = networkHandler.getConnection(0).parseReceivedPackets();
        for (IPacket packet : packets) {
            System.out.println("Recieved packet : " + packet.toString());
        }
    }
}
