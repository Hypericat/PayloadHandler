package Client;

import Client.Networking.ClientNetworkHandler;
import NetworkUtils.NetworkUtil;

import java.util.concurrent.TimeUnit;

public class Client {

    public static final int secondsRetryConnect = 0;
    public static final int msRetryConnect = 0;
    public static final int msRetryFinalConnect = secondsRetryConnect * 1000 + msRetryConnect;

    private static ClientNetworkHandler networkHandler;

    public static void run() {
        System.out.println("Running client!");
        networkHandler = new ClientNetworkHandler();

        //Init connection
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
    }
}
