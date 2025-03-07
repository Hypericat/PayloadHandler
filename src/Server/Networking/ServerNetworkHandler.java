package Server.Networking;

import NetworkUtils.PacketHandler;
import NetworkUtils.SocketConnection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.stream.Stream;

public class ServerNetworkHandler {
    private final HashMap<SocketConnection, ServerClient> connections = new HashMap<>();
    private int nextClientId = 1;
    private ServerSocket serverSocket;

    public boolean readConnections(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();


            SocketConnection connection = new SocketConnection(socket);

            PacketHandler packetHandler = new PacketHandler(connection);

            ServerClient client = new ServerClient(connection, this);

            connections.put(connection, client);

            System.out.println("Client connected and assigned ID: " + nextClientId);

            nextClientId++;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ServerClient getClientById(int clientId) {
        return connections.values().stream().skip(clientId - 1).findFirst().orElse(null);
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

    public Stream<SocketConnection> getConnections() {
        return this.connections.keySet().stream();
    }

    public Stream<ServerClient> getClients() {
        return this.connections.values().stream();
    }
}
