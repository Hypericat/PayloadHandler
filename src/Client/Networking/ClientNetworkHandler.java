package Client.Networking;

import NetworkUtils.SocketConnection;

import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientNetworkHandler {
    private SocketConnection connection;
    private static final int CONNECT_TIMEOUT = 5000;

    public ClientNetworkHandler() {

    }

    // Connect to server with timeout
    public boolean connect(short port, String serverHostname) {
        try {
            InetAddress serverAddress = InetAddress.getByName(serverHostname);
            System.out.println("Resolved IP: " + serverAddress.getHostAddress());

            // Create new socket and connect with timeout
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, port), CONNECT_TIMEOUT);

            connection = new SocketConnection(socket);

            System.out.println("Connected to the server.");
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    // Close connection and streams
    public void close() {
        connection.close();
    }

    public SocketConnection getConnection() {
        return this.connection;
    }
}
