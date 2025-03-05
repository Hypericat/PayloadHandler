package Server.Networking;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerNetworkHandler {
    private ServerSocket socket;

    public boolean readConnections(short port) {
        try {
            socket = new ServerSocket(port);
            socket.accept();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void close() {
        if (socket.isClosed()) return; // Socket is currently not connected so why would we disconnect
        try {
        socket.close();
        } catch (IOException e) {
            return;
        }
    }
}
