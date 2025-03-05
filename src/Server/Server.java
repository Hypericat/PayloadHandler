package Server;

import NetworkUtils.NetworkUtil;
import Server.Networking.ServerNetworkHandler;

public class Server {
    private static ServerNetworkHandler networkHandler;

    public static void run() {
        System.out.println("Running server!");
        networkHandler = new ServerNetworkHandler();

        while(true) {
            if (networkHandler.readConnections(NetworkUtil.port)) {
                System.out.println("Connected!");
                networkHandler.close();
                return;
            }
            System.out.println("Failed to connect!");
        }
    }
}
