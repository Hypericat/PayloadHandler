package Server.Networking;

import NetworkUtils.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerNetworkHandler {
    private final List<SocketConnection> connections = new ArrayList<>();
    ServerSocket serverSocket;

    public boolean readConnections(short port) {
        try {
            serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            connections.add(new SocketConnection(socket));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public SocketConnection getConnection(int index) {
        return this.connections.get(index);
    }

    public int getConnectionCount() {
        return this.connections.size();
    }

    public void close() {
        connections.forEach(SocketConnection::close);
    }
}
