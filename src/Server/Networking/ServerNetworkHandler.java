package Server.Networking;

import NetworkUtils.SocketConnection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.stream.Stream;

public class ServerNetworkHandler {
    private static final HashMap<SocketConnection, ServerClient> connections = new HashMap<>();
    private int nextClientId = 1;
    private ServerSocket serverSocket;

    public boolean readConnections(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();

            SocketConnection connection = new SocketConnection(socket);
            String userFolder = System.getProperty("user.home");
            ServerClient client = new ServerClient(connection, this, nextClientId, userFolder);
            Server.Server.setSelectedClient(client);
            connections.put(connection, client);

            System.out.println("Client connected and assigned ID: " + nextClientId);

            nextClientId++;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ServerClient getClientById(int clientId) {
        for (ServerClient client : connections.values()) {
            if (client.getId() == clientId) {
                return client;
            }
        }
        return null;
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
