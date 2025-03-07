package Server.Networking;

import NetworkUtils.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerNetworkHandler {
    private final HashMap<SocketConnection, ServerClient> connections = new HashMap<>();
    ServerSocket serverSocket;

    public boolean readConnections(short port) {
        try {
            serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            SocketConnection connection = new SocketConnection(socket);
            connections.put(connection, new ServerClient(connection, this));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public SocketConnection getConnection(int index) {
        return this.connections.values().stream().toList().get(index).getConnection();
    }

    public int getConnectionCount() {
        return this.connections.size();
    }

    public void close(SocketConnection connection) {
        connection.close();
        connections.remove(connection);
    }

    public void closeAll() {
        connections.keySet().forEach(SocketConnection::close);
        connections.clear();
    }
}
