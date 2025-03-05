package Client;

import NetworkUtils.ClientNetworkingHandler;

import static NetworkUtils.ClientNetworkingHandler.serverHostname;
import static NetworkUtils.ClientNetworkingHandler.serverPort;

public class Client {

    public static final int secondsRetryConnect = 15;
    public static final int msRetryConnect = 0;
    public static final int msRetryFinalConnect = secondsRetryConnect * 1000 + msRetryConnect;

    public static void run() {

        ClientNetworkingHandler networkingHandler = new ClientNetworkingHandler(serverHostname, serverPort, msRetryFinalConnect);
        networkingHandler.connect();  // start connection

    }
}
